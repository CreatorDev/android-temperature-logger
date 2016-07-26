/*
 * <b>Copyright 2015 by Imagination Technologies Limited
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


import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.imgtec.creator.petunia.BuildConfig;
import com.imgtec.di.HasComponent;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric.sdk.android.Fabric;


/**
 *
 */
public class App extends Application implements HasComponent<ApplicationComponent> {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private ApplicationComponent component;
  private RefWatcher refWatcher;

  @Override
  public void onCreate() {
    super.onCreate();

    component = ApplicationComponent.Initializer.init(this);
    component.inject(this);

    refWatcher = installLeakCanary();

    initCrashlytics();
    logger.debug("---> Application created: {}", this);
  }

  private RefWatcher installLeakCanary() {
    if (BuildConfig.DEBUG) {
      return LeakCanary.install(this);
    }
    else {
      return RefWatcher.DISABLED;
    }
  }

  private void initCrashlytics() {
    if (BuildConfig.USE_CRASHLYTICS) {
      Fabric.with(this, new Crashlytics());
    }
  }

  @Override
  public ApplicationComponent getComponent() {
    return component;
  }
}
