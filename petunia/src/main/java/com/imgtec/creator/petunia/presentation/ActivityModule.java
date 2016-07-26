package com.imgtec.creator.petunia.presentation;

import com.imgtec.creator.petunia.presentation.utils.ToolbarHelper;
import com.imgtec.di.PerActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module
final class ActivityModule {

  private final MainActivity activity;

  public ActivityModule(MainActivity activity) {
    this.activity = activity;
  }

  @Provides @PerActivity
  Logger providesLogger() {
    return LoggerFactory.getLogger(activity.getClass().getSimpleName());
  }

  @Provides
  MainActivity provideActivity() {
    return activity;
  }

  @Provides @PerActivity
  ToolbarHelper provideToolbarHelper(MainActivity activity) {
    return new ToolbarHelper(activity);
  }
}
