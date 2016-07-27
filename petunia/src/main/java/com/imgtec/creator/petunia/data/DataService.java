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

import java.util.Date;
import java.util.List;

/**
 *
 */
public interface DataService {

  interface DataCallback<T> {

    void onSuccess(final DataService service, final T result);
    void onFailure(final DataService service, final Throwable t);
  }

  interface DataCallback2<T1, T2> {

    void onSuccess(final DataService service, final T1 param, final T2 result);
    void onFailure(final DataService service, final T1 param, final Throwable t);
  }
  void requestSensors(final DataCallback<List<Sensor>> callback);

  Measurement getLastMeasurementForSensor(final Sensor sensor);

  void requestMeasurements(Sensor sensor, Date from, Date to, long interval,
                           DataCallback2<Sensor, List<Measurement>> callback);

  void requestMeasurements(Date from, Date to, long interval,
                           DataCallback2<List<Sensor>, List<Measurement>> callback);
}
