/*
 * <b>Copyright 2016 by Imagination Technologies Limited
 * and/or its affiliated group companies.</b>
 *
 * All rights reserved.  No part of this software, either
 * material or conceptual may be copied or distributed,
 * transmitted, transcribed, stored in a retrieval system
 * or translated into any human or computer language in any
 * form by any means, electronic, mechanical, manual or
 * other-wise, or disclosed to the third parties without the
 * express written permission of Imagination Technologies
 * Limited, Home Park Estate, Kings Langley, Hertfordshire,
 * WD4 8LZ, U.K.
 */

package com.imgtec.creator.petunia.data;

import android.content.Context;
import android.os.Handler;

import com.imgtec.creator.petunia.data.api.ApiService;
import com.imgtec.creator.petunia.data.api.pojo.Client;
import com.imgtec.creator.petunia.data.api.pojo.Clients;
import com.imgtec.creator.petunia.data.api.pojo.Data;
import com.imgtec.creator.petunia.data.api.pojo.Measurements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 */
public class DataServiceImpl implements DataService {

  final Logger logger = LoggerFactory.getLogger(getClass());
  static final Measurement DUMMY_MEASUREMENT = new Measurement("N/A", 0, new Date());
  static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

  final Context context;
  final ScheduledExecutorService executor;
  final Handler mainHandler;
  final ApiService apiService;
  final Map<Sensor, Measurement> sensorMeasurementMap = new HashMap<>();

  static {
    dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
  }


  DataServiceImpl(Builder builder) {
    super();
    this.context = builder.getAppContext();
    this.executor = builder.getExecutor();
    this.mainHandler = builder.getMainHandler();
    this.apiService = builder.getApiService();
  }


  @Override
  public void requestSensors(final DataCallback<List<Sensor>> callback) {
    executor.execute(new Runnable() {
      @Override
      public void run() {

        try {
          final Clients clients = apiService.getClients(new ApiService.Filter<Client>() {
            @Override
            public boolean accept(Client client) {
              return true;
            }
          });

          final List<Sensor> sensors = new ArrayList<>(clients.getItems().size());
          for (Client c: clients.getItems()) {
            final Data data = c.getData().get(0); //FIXME: refactor JSON structure
            final Sensor sensor = new Sensor(data.getId(), data.getClientName());
            final Measurement m = new Measurement(sensor.getId(),
                                                  Float.parseFloat(data.getData()),
                                                  dateFormatter.parse(data.getTimestamp()));

            synchronized (sensorMeasurementMap) {
              sensorMeasurementMap.put(sensor, m);
            }
            sensors.add(sensor);
          }

          mainHandler.post(new Runnable() {
            @Override
            public void run() {
              callback.onSuccess(DataServiceImpl.this, sensors);
            }
          });
        }
        catch (final Exception e) {
          logger.warn("Requesting sensors failed!", e);
          mainHandler.post(new Runnable() {
            @Override
            public void run() {
              callback.onFailure(DataServiceImpl.this, new IllegalStateException("Not yet implemented"));
            }
          });
        }
      }
    });
  }

  @Override
  public Measurement getLastMeasurementForSensor(final Sensor sensor) {
    Measurement m;
    synchronized (sensorMeasurementMap) {
      m = sensorMeasurementMap.get(sensor);
    }
    if (m == null) {
      return DUMMY_MEASUREMENT;
    }
    return m;
  }

  @Override
  public void requestMeasurements(final Sensor sensor,
                                  final Date from,
                                  final Date to,
                                  final long interval,
                                  final DataCallback2<Sensor, List<Measurement>> callback) {
    executor.execute(new Runnable() {
      @Override
      public void run() {

        try {
          final Measurements measurements = apiService.getMeasurements(sensor.getId(), from, to);
          final List<Measurement> measurementList = new ArrayList<>();
          if (measurements.getMeasurements().size() > 0) {
            Measurement lastMeasurement = new Measurement(sensor.getId(),
                Float.parseFloat(measurements.getMeasurements().get(0).getValue()),
                dateFormatter.parse(measurements.getMeasurements().get(0).getTimestamp()));

            measurementList.add(lastMeasurement);

            for (com.imgtec.creator.petunia.data.api.pojo.Measurement m : measurements.getMeasurements()) {
              Date date = dateFormatter.parse(m.getTimestamp());
              if (Math.abs(lastMeasurement.getTimestamp().getTime() - date.getTime()) >= interval * 1000) {

                Measurement measurement = new Measurement(sensor.getId(),
                    Float.parseFloat(m.getValue()), dateFormatter.parse(m.getTimestamp()));
                measurementList.add(measurement);
                lastMeasurement = measurement;
              }
            }
          }

          mainHandler.post(new Runnable() {
            @Override
            public void run() {
              callback.onSuccess(DataServiceImpl.this, sensor, measurementList);
            }
          });
        }
        catch (Exception e) {
          logger.warn("Requesting measurements for sensor: {} -> <{}, {}> failed!",
              sensor.getId(), from, to, e);

          mainHandler.post(new Runnable() {
            @Override
            public void run() {
              callback.onFailure(DataServiceImpl.this, sensor, new IllegalStateException("Not yet implemented"));
            }
          });

        }

      }
    });
  }

  @Override
  public void requestMeasurements(Date from, Date to, long interval,
                                  DataCallback2<List<Sensor>, List<Measurement>> callback) {

  }



  public static class Builder {

    private Context context;
    private ScheduledExecutorService executor;
    private Handler handler;
    private ApiService apiService;

    public DataService build() {
      return new DataServiceImpl(this);
    }

    public Builder setAppContext(Context context) {
      this.context = context;
      return this;
    }

    public Context getAppContext() {
      return context;
    }

    public Builder setExecutor(ScheduledExecutorService executor) {
      this.executor = executor;
      return this;
    }

    public ScheduledExecutorService getExecutor() {
      return executor;
    }

    public Builder setMainHandler(Handler handler) {
      this.handler = handler;
      return this;
    }

    public Handler getMainHandler() {
      return handler;
    }

    public Builder setApiService(ApiService apiService) {
      this.apiService = apiService;
      return this;
    }

    ApiService getApiService() {
      return apiService;
    }
  }
}
