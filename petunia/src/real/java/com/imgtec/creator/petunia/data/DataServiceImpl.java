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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 */
public class DataServiceImpl implements DataService {

  final Logger logger = LoggerFactory.getLogger(getClass());
  final Measurement DUMMY_MEASUREMENT = new Measurement("dummy", 0, new Date());

  final Context context;
  final ScheduledExecutorService executor;
  final Handler mainHandler;
  final ApiService apiService;

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
            final Data data = c.getData().get(0);
            sensors.add(new Sensor(data.getId(), data.getClientName()));
          }

          //TODO: implement

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
    return DUMMY_MEASUREMENT;
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
        //TODO: implement

        mainHandler.post(new Runnable() {
          @Override
          public void run() {
            callback.onFailure(DataServiceImpl.this, sensor, new IllegalStateException("Not yet implemented"));
          }
        });
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
