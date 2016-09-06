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

package com.imgtec.creator.petunia.data.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imgtec.creator.petunia.app.App;
import com.imgtec.di.PerApp;

import java.io.File;
import java.util.concurrent.ExecutorService;

import javax.inject.Named;

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
  private static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUg" +
      "SldUIEJ1aWxkZXIiLCJpYXQiOjE0Njg0MTQxMDAsImV4cCI6MTQ5OTk1MDEwMCwiYXVkIjoid3d3LmV4YW1wbGUuY" +
      "29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2" +
      "NrZXQiLCJFbWFpbCI6Impyb2NrZXRAZXhhbXBsZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWl" +
      "uaXN0cmF0b3IiXX0.KTzci15ZjTlNDbJf93rbovSyX_F-4NINaqh6-Q77nW0";

  @Provides @PerApp @Named("Auth")
  String provideAuthToken() {
    return TOKEN;
  }

  @Provides @PerApp
  AuthInterceptor provideAuthInterceptor(@Named("Auth") final String authToken) {
    return new AuthInterceptor(authToken);
  }

  @Provides @PerApp
  OkHttpClient provideOkHttpClient(App app, AuthInterceptor oauthInterceptor) {
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
