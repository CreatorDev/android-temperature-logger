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

/**
 *
 */
public class Measurement {

  public static final int TYPE = 0;
  private final String sensorId;
  private final float value;
  private final Date timestamp;

  public Measurement(String sensorId, float value, Date timestamp) {
    super();
    this.sensorId = sensorId;
    this.value = value;
    this.timestamp = timestamp;
  }

  public String getSensorId() {
    return sensorId;
  }

  public float getValue() {
    return value;
  }

  public int getType() {
    return TYPE;
  }

  public Date getTimestamp() {
    return timestamp;
  }
}
