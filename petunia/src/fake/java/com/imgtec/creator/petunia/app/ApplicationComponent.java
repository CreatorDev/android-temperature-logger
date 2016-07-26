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

package com.imgtec.creator.petunia.app;

import android.content.SharedPreferences;
import android.os.Handler;

import com.imgtec.creator.petunia.data.DataService;
import com.imgtec.creator.petunia.data.FakeDataModule;
import com.imgtec.creator.petunia.data.api.ApiModule;
import com.imgtec.creator.petunia.data.api.ApiService;
import com.imgtec.creator.petunia.data.api.oauth.OauthModule;
import com.imgtec.creator.petunia.data.api.oauth.OauthTokenWrapper;
import com.imgtec.di.PerApp;

import javax.inject.Named;

import dagger.Component;
import okhttp3.OkHttpClient;

@PerApp
@Component(
    modules = {
        ApplicationModule.class,
        FakeDataModule.class,
        OauthModule.class,
        ApiModule.class
    }
)
public interface ApplicationComponent {

  final class Initializer {

    private Initializer() {}

    static ApplicationComponent init(App application) {
      return DaggerApplicationComponent
          .builder()
          .applicationModule(new ApplicationModule(application))
          .oauthModule(new OauthModule())
          .build();
    }
  }

  App inject(App app);

  SharedPreferences getSharedPreferences();

  @Named("Main") Handler getHandler();

  DataService getDataService();

  OkHttpClient getOkHttpClient();

  ApiService getApiService();

  OauthTokenWrapper getOauthTokenWrapper();
}

