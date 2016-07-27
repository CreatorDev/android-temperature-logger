package com.imgtec.creator.petunia.app;

import android.content.Context;
import android.os.Handler;

import com.imgtec.di.PerApp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module
public final class ApplicationModule {

  final App application;

  public ApplicationModule(final App app) {
    this.application = app;
  }

  @Provides @PerApp
  App provideApplication() {
    return application;
  }

  @Provides @PerApp
  Context provideAppContext() {
    return application;
  }

  @Provides @PerApp
  ExecutorService provideExecutorService() {
    return Executors.newFixedThreadPool(4);
  }

  @Provides @PerApp @Named("Main")
  Handler provideHandler() {
    return new Handler(application.getMainLooper());
  }

}
