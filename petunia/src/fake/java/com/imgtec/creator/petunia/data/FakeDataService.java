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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FakeDataService implements DataService {

  final Context context;
  final ScheduledExecutorService executor;
  final Handler mainHandler;
  final Random random;

  private FakeDataService(Builder builder) {
    super();
    this.context = builder.getAppContext();
    this.executor = builder.getExecutor();
    this.mainHandler = builder.getMainHandler();
    this.random = new Random();
  }

  @Override
  public void startPollingForSensorChanges(DataCallback<List<Sensor>> callback) {
    //skip
  }

  @Override
  public void stopPolling() {
    //skip
  }

  @Override
  public void requestSensors(final DataCallback<List<Sensor>> callback) {
    executor.schedule(new Runnable() {
      @Override
      public void run() {
        final List<Sensor> list = generateDummySensors(5);
        mainHandler.post(new Runnable() {
          @Override
          public void run() {
            callback.onSuccess(FakeDataService.this, list);
          }
        });

      }
    }, 2, TimeUnit.SECONDS);
  }

  List<Sensor> generateDummySensors(final int count) {
    final List<Sensor> list = new ArrayList<Sensor>(count);
    for (int i = 0; i < count; ++i) {
      list.add(new Sensor(""+i, "Sensor " + i, "0.5"));
    }
    return list;
  }

  @Override
  public Measurement getLastMeasurementForSensor(final Sensor sensor) {
    return new Measurement(sensor.getId(), 20.f+random.nextFloat(), new Date());
  }

  @Override
  public void requestMeasurements(final Sensor sensor,
                                  final Date from,
                                  final Date to,
                                  final long interval,
                                  final DataCallback2<Sensor, List<Measurement>> callback) {
    executor.schedule(new Runnable() {
      @Override
      public void run() {

        final List<Measurement> list = generateDummyMeasurements(sensor);
        mainHandler.post(new Runnable() {
          @Override
          public void run() {
            callback.onSuccess(FakeDataService.this, sensor, list);
          }
        });

      }
    }, 2, TimeUnit.SECONDS);
  }

  private List<Measurement> generateDummyMeasurements(final Sensor s) {
    final List<Measurement> list = new ArrayList<Measurement>();
    long timestamp = System.currentTimeMillis();
    for (int i = 0; i < 100; ++i) {
      final float value = 20.f + 10*random.nextFloat();
      final Date date = new Date(timestamp + 60000*i);
      list.add(new Measurement(s.getId(), value, date));
    }
    return list;
  }

  @Override
  public void requestMeasurements(Date from, Date to, long interval,
                                  final DataCallback2<List<Sensor>, List<Measurement>> callback) {
    executor.schedule(new Runnable() {
      @Override
      public void run() {
        final List<Measurement> measurements = new ArrayList<Measurement>();
        final List<Sensor> sensors = generateDummySensors(5);
        for (Sensor s : sensors) {
          measurements.addAll(generateDummyMeasurements(s));
        }

        Collections.sort(measurements, new Comparator<Measurement>() {
          @Override
          public int compare(Measurement lhs, Measurement rhs) {
            return lhs.getTimestamp().compareTo(rhs.getTimestamp());
          }
        });

        mainHandler.post(new Runnable() {
          @Override
          public void run() {
            callback.onSuccess(FakeDataService.this, sensors, measurements);
          }
        });
      }
    }, 5, TimeUnit.SECONDS);
  }

  @Override
  public void setDelta(Sensor sensor, float delta, DataCallback<Sensor> callback) {
    //skip
  }

  @Override
  public void clearAllMeasurements(DataCallback<Void> callback) {
    //skip
  }

  public static class Builder {

    private Context context;
    private ScheduledExecutorService executor;
    private Handler handler;

    public DataService build() {
      return new FakeDataService(this);
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

  }
}
