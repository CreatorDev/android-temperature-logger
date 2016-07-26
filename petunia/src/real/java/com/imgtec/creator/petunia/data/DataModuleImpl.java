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

import android.os.Handler;
import android.support.annotation.NonNull;

import com.imgtec.creator.petunia.app.App;
import com.imgtec.creator.petunia.data.api.ApiService;
import com.imgtec.creator.petunia.data.api.ApiServiceImpl;
import com.imgtec.di.PerApp;

import java.util.concurrent.ScheduledExecutorService;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;


@Module(
    includes = {
        DataModule.class
    }
)
public final class DataModuleImpl {


  @Provides @PerApp
  DataService provideDataService(@NonNull App application,
                                 @NonNull ScheduledExecutorService executorService,
                                 @NonNull @Named("Main") Handler handler,
                                 @NonNull ApiService apiService) {

    return new DataServiceImpl.Builder()
        .setAppContext(application)
        .setExecutor(executorService)
        .setMainHandler(handler)
        .setApiService(apiService)
        .build();
  }

}
