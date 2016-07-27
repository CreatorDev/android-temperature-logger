package com.imgtec.creator.petunia.presentation;

import com.imgtec.creator.petunia.app.ApplicationComponent;
import com.imgtec.creator.petunia.presentation.fragments.dashboard.DashboardFragment;
import com.imgtec.creator.petunia.presentation.fragments.dashboard.DetailsFragment;
import com.imgtec.creator.petunia.presentation.fragments.dashboard.HistoryFragment;
import com.imgtec.creator.petunia.presentation.utils.ToolbarHelper;
import com.imgtec.di.HasComponent;
import com.imgtec.di.PerActivity;

import dagger.Component;

@PerActivity
@Component(
    dependencies = ApplicationComponent.class,
    modules = {
        ActivityModule.class
    }
)
public interface ActivityComponent {

  class Initializer {

    private Initializer() {}

    static ActivityComponent init(MainActivity activity) {
      return DaggerActivityComponent
          .builder()
          .applicationComponent(((HasComponent<ApplicationComponent>) activity.getApplicationContext()).getComponent())
          .activityModule(new ActivityModule(activity))
          .build();
    }
  }

  void inject(MainActivity activity);
  void inject(DashboardFragment fragment);
  void inject(DetailsFragment fragment);
  void inject(HistoryFragment fragment);

  ToolbarHelper getToolbarHelper();
}
