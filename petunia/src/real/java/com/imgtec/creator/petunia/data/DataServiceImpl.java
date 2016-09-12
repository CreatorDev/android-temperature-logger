/*
 * <b>Copyright (c) 2016, Imagination Technologies Limited and/or its affiliated group companies
 *  and/or licensors. </b>
 *
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are permitted
 *  provided that the following conditions are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *      and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *      conditions and the following disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *
 *  3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *      endorse or promote products derived from this software without specific prior written
 *      permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 *  DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 *  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 *  WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.imgtec.creator.petunia.data;

import android.content.Context;
import android.os.Handler;

import com.imgtec.creator.petunia.data.api.ApiService;
import com.imgtec.creator.petunia.data.api.pojo.Client;
import com.imgtec.creator.petunia.data.api.pojo.Clients;
import com.imgtec.creator.petunia.data.api.pojo.ClientData;
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
  static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

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
            final ClientData data = c.getData();
            final Sensor sensor = new Sensor(data.getId(), data.getClientName(), data.getDelta());
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
              callback.onFailure(DataServiceImpl.this, e);
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
                Float.parseFloat(measurements.getMeasurements().get(0).getData().getValue()),
                dateFormatter.parse(measurements.getMeasurements().get(0).getData().getTimestamp()));

            measurementList.add(lastMeasurement);

            for (com.imgtec.creator.petunia.data.api.pojo.Measurement m : measurements.getMeasurements()) {
              Date date = dateFormatter.parse(m.getData().getTimestamp());
              if (Math.abs(lastMeasurement.getTimestamp().getTime() - date.getTime()) >= interval * 1000) {

                Measurement measurement = new Measurement(sensor.getId(),
                    Float.parseFloat(m.getData().getValue()),
                    dateFormatter.parse(m.getData().getTimestamp()));

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
        catch (final Exception e) {
          logger.warn("Requesting measurements for sensor: {} -> <{}, {}> failed!",
              sensor.getId(), from, to, e);

          mainHandler.post(new Runnable() {
            @Override
            public void run() {
              callback.onFailure(DataServiceImpl.this, sensor, e);
            }
          });

        }

      }
    });
  }

  @Override
  public void requestMeasurements(final Date from,
                                  final Date to,
                                  final long interval,
                                  final DataCallback2<List<Sensor>, List<Measurement>> callback) {
    executor.execute(new Runnable() {
      @Override
      public void run() {
        //TODO: implement

        mainHandler.post(new Runnable() {
          @Override
          public void run() {
            callback.onFailure(DataServiceImpl.this, new ArrayList<Sensor>(), new IllegalStateException("Not yet implemented"));
          }
        });
      }
    });
  }

  @Override
  public void setDelta(final Sensor sensor, final float delta, final DataCallback<Sensor> callback) {

    executor.execute(new Runnable() {
      @Override
      public void run() {
        try {
          apiService.setDelta(sensor.getId(), delta);

          sensor.setDelta(String.valueOf(delta));

          mainHandler.post(new Runnable() {
            @Override
            public void run() {
              callback.onSuccess(DataServiceImpl.this, sensor);
            }
          });
        } catch (final Exception e) {
          logger.warn("Setting delta {} for sensor {} failed", delta, sensor.getId(), e);
          mainHandler.post(new Runnable() {
            @Override
            public void run() {
              callback.onFailure(DataServiceImpl.this, e);
            }
          });
        }
      }
    });
  }

  @Override
  public void clearAllMeasurements(final DataCallback<Void> callback) {
    executor.execute(new Runnable() {
      @Override
      public void run() {
        try {

          apiService.clearAllMeasurements();

          mainHandler.post(new Runnable() {
            @Override
            public void run() {
              callback.onSuccess(DataServiceImpl.this, null);
            }
          });
        } catch (final Exception e) {
          logger.warn("Clear all measurements failed!", e);
          mainHandler.post(new Runnable() {
            @Override
            public void run() {
              callback.onFailure(DataServiceImpl.this, e);
            }
          });
        }
      }
    });
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
