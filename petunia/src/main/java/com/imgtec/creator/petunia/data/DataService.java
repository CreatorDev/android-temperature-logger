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

  void startPollingForSensorChanges(final DataCallback<List<Sensor>> callback);
  void stopPolling();

  void clearCache();
  void requestSensors(final DataCallback<List<Sensor>> callback);

  Measurement getLastMeasurementForSensor(final Sensor sensor);

  void requestMeasurements(Sensor sensor, Date from, Date to, long interval,
                           DataCallback2<Sensor, List<Measurement>> callback);

  void requestMeasurements(Date from, Date to, long interval,
                           DataCallback2<List<Sensor>, List<Measurement>> callback);

  void setDelta(Sensor sensor, float delta, DataCallback<Sensor> callback);

  void clearAllMeasurements(DataCallback<Void> callback);
}
