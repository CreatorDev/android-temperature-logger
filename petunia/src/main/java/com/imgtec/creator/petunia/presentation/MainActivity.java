package com.imgtec.creator.petunia.presentation;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import com.imgtec.creator.petunia.R;
import com.imgtec.creator.petunia.data.Preferences;
import com.imgtec.creator.petunia.presentation.fragments.FragmentHelper;
import com.imgtec.creator.petunia.presentation.fragments.dashboard.DashboardFragment;
import com.imgtec.creator.petunia.presentation.utils.ToolbarHelper;
import com.imgtec.di.HasComponent;

import org.slf4j.Logger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 */
public class MainActivity extends BaseActivity implements HasComponent<ActivityComponent> {

  private ActivityComponent component;

  @BindView(R.id.app_bar) AppBarLayout appBar;
  @BindView(R.id.toolbar) Toolbar toolbar;

  Fragment currentFragment;

  @Inject Logger logger;

  @Inject ToolbarHelper toolbarHelper;
  @Inject Preferences preferences;

  Unbinder unbinder;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    unbinder = ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    toolbarHelper.setToolbar(toolbar);

    if (savedInstanceState == null) {
      showFragmentWithClearBackstack(DashboardFragment.newInstance());
    }
  }

  @Override
  protected void setComponent() {
    component = ActivityComponent.Initializer.init(this);
    component.inject(this);
  }

  @Override
  public ActivityComponent getComponent() {
    return component;
  }

  @Override
  public void onAttachFragment(Fragment fragment) {
    super.onAttachFragment(fragment);
    currentFragment = fragment;
  }

  @Override
  protected void onDestroy() {
    unbinder.unbind();
    toolbarHelper = null;
    super.onDestroy();
  }


  @Override
  public void onBackPressed() {
    final FragmentManager fm = getSupportFragmentManager();
    if (fm.getBackStackEntryCount() > 0) {
      fm.popBackStack();
    }
    else {
      super.onBackPressed();
    }
    currentFragment = null;
  }

  private void showFragmentWithClearBackstack(Fragment f) {
    currentFragment = f;
    FragmentHelper.replaceFragmentAndClearBackStack(getSupportFragmentManager(), currentFragment);
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
  }
}
