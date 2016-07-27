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

package com.imgtec.creator.petunia.data.api.oauth;

import com.imgtec.creator.petunia.data.api.pojo.OauthToken;
import com.imgtec.di.PerApp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 */
@PerApp
public final class OauthInterceptor implements Interceptor {

  private final OauthTokenWrapper authTokenWrapper;

  public OauthInterceptor(OauthTokenWrapper tokenWrapper) {
    super();
    this.authTokenWrapper = tokenWrapper;
  }

  @Override public Response intercept(Chain chain) throws IOException {

    OauthToken token = authTokenWrapper.getAuthToken();
    Request.Builder builder = chain.request().newBuilder();
    if (token != null) {
      builder.addHeader("Authorization",
          String.format("%s %s", token.getTokenType(), token.getAccessToken()));
    }
    Request request = builder.build();
    return chain.proceed(request);
  }
}
