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

package com.imgtec.creator.petunia.presentation.adapters;

import com.imgtec.creator.petunia.data.Measurement;
import com.imgtec.creator.petunia.data.Sensor;

import java.io.Serializable;

/**
 *
 */
public class DashboardItem implements Serializable {

  private final Sensor sensor;
  private final Measurement measurement;

  public DashboardItem(Sensor s, Measurement m) {
    this.sensor = s;
    this.measurement = m;
  }

  public String getTitle() {
    return sensor.getName();
  }

  public String getValue() {
    return measurement.getValue();
  }

  public int getType() {
    return measurement.getType();
  }

  public Sensor getSensor() {
    return sensor;
  }

}
