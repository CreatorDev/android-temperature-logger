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

package com.imgtec.creator.petunia.data.api;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.imgtec.creator.petunia.data.api.pojo.Hateoas;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 *
 */
public class ResponseHandler {

  public <T extends Hateoas> T handle(Request request, Response response, Class<T> returnType)
      throws IOException {

    if (response.isSuccessful()) {
      return new GsonBuilder()
          .create()
          .fromJson(response.body().string(), returnType);
    }
    //TODO: handle response if possible

    throw new RuntimeException("Request failed with code:" + response.code());
  }

  public <T extends Hateoas> T handle(Request request, Response response, TypeToken<T> typeToken)
      throws IOException {
    if (response.isSuccessful()) {
      return new GsonBuilder()
          .create()
          .fromJson(response.body().string(), typeToken.getType());
    }
    throw new RuntimeException("Request failed with code:" + response.code());
  }
}