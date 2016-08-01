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

package com.imgtec.creator.petunia.data.api.requests;

import com.google.gson.GsonBuilder;
import com.imgtec.creator.petunia.data.api.pojo.Delta;
import com.imgtec.creator.petunia.data.api.pojo.DeltaData;

import java.util.Date;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 *
 */
public class UpdateDelta extends BaseRequest<Delta> {

  private final double delta;
  private final String timestamp;

  public UpdateDelta(String url, double delta, String timestamp) {
    super(url);
    this.delta = delta;
    this.timestamp = timestamp;
  }

  @Override
  public Request prepareRequest() {


    DeltaData data = new DeltaData();
    data.setDelta(String.valueOf(delta));
    data.setTimestamp(timestamp);
    Delta d = new Delta();
    d.setData(data);

    HttpUrl url = HttpUrl.parse(getUrl());

    Request.Builder builder = new Request.Builder();
    builder.url(url
        .newBuilder()
        .build())
        .put(RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            new GsonBuilder().create().toJson(d)))
        .build();

    return builder.build();
  }

}
