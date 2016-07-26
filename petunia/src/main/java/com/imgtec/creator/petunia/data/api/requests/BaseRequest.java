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

import com.imgtec.creator.petunia.data.api.ResponseHandler;
import com.imgtec.creator.petunia.data.api.pojo.Hateoas;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 *
 */
public abstract class BaseRequest<T extends Hateoas> {

  private final String url;

  public BaseRequest(String url) {
    super();
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public abstract Request prepareRequest();

  public T execute(OkHttpClient client, Class<T> clazz) throws IOException {
    Request request = prepareRequest();
    okhttp3.Response response = client.newCall(request).execute();
    return new ResponseHandler().handle(request, response, clazz);
  }
}
