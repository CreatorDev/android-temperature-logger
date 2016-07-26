package com.imgtec.creator.petunia.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.imgtec.creator.petunia.app.App;
import com.imgtec.di.PerApp;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module
public class DataModule {

  static final String PREFS = "data";

  @Provides @PerApp
  SharedPreferences provideSharedPreferences(App application) {
    return application.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
  }

  @Provides @PerApp
  Preferences providesPreferences(@NonNull final SharedPreferences prefs) {
    return new Preferences(prefs);
  }

  @Provides @PerApp
  ScheduledExecutorService provideScheduleExecutorService() {
    return Executors.newScheduledThreadPool(4);
  }
}
