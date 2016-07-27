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

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imgtec.creator.petunia.app.App;
import com.imgtec.creator.petunia.data.api.oauth.OauthInterceptor;
import com.imgtec.di.PerApp;

import java.io.File;
import java.util.concurrent.ExecutorService;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 *
 */
@Module
public class ApiModule {

  private static final long CACHE_DISK_SIZE = 50 * 1024 * 1024;


  @Provides @PerApp
  OkHttpClient provideOkHttpClient(App app, OauthInterceptor oauthInterceptor) {
    File cacheDir = new File(app.getCacheDir(), "http");
    Cache cache = new Cache(cacheDir, CACHE_DISK_SIZE);

    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient okHttpClient = new OkHttpClient
        .Builder()
        .cache(cache)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(oauthInterceptor)
        .build();
    return okHttpClient;
  }

  @Provides @PerApp
  HttpUrl provideBaseUrl() {
    return HttpUrl.parse("http://kiwano.herokuapp.com/api/v1/");
  }

  @Provides @PerApp
  Gson provideGson() {
    Gson gson = new GsonBuilder()
        .create();
    return gson;
  }

  @Provides @PerApp
  ApiService provideApiService(final Context appContext,
                               final HttpUrl url,
                               final OkHttpClient client,
                               final ExecutorService executorService) {
    return new ApiServiceImpl(appContext, url, client, executorService);
  }
}
