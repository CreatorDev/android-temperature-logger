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

package com.imgtec.creator.petunia.presentation.fragments.dashboard;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.imgtec.creator.petunia.R;
import com.imgtec.creator.petunia.data.DataService;
import com.imgtec.creator.petunia.data.Measurement;
import com.imgtec.creator.petunia.data.Sensor;
import com.imgtec.creator.petunia.presentation.ActivityComponent;
import com.imgtec.creator.petunia.presentation.adapters.DashboardAdapter;
import com.imgtec.creator.petunia.presentation.adapters.DashboardItem;
import com.imgtec.creator.petunia.presentation.fragments.BaseFragment;
import com.imgtec.creator.petunia.presentation.fragments.FragmentHelper;
import com.imgtec.creator.petunia.presentation.views.RecyclerItemClickSupport;
import com.imgtec.creator.petunia.presentation.views.SpaceItemDecoration;
import com.imgtec.di.HasComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 */
public class DashboardFragment extends BaseFragment {

  final Logger logger = LoggerFactory.getLogger(getClass());

  @BindView(R.id.recycler_view) RecyclerView recyclerView;

  @Inject DataService dataService;

  private DashboardAdapter adapter;


  public static Fragment newInstance() {
    DashboardFragment fragment = new DashboardFragment();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      fragment.setExitTransition(new Slide(Gravity.LEFT));
    }
    return fragment;
  }

  @Override
  protected void setComponent() {
    ((HasComponent<ActivityComponent>) getActivity()).getComponent().inject(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      setExitTransition(new Explode());
    }

    return v;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    adapter = new DashboardAdapter();

    GridLayoutManager gridLayoutManager = new DashboardGridLayoutManager(getContext(),
        getResources().getInteger(R.integer.dashboard_columns_count));
    recyclerView.setLayoutManager(gridLayoutManager);
    recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin)));
    recyclerView.setItemAnimator(new DashboardItemAnimator());
    recyclerView.setAdapter(adapter);

    RecyclerItemClickSupport.addTo(recyclerView)
        .setOnItemClickListener(new RecyclerItemClickSupport.OnItemClickListener() {

          @TargetApi(21)
          @Override
          public void onItemClicked(RecyclerView recyclerView, int position, View view) {
            DashboardItem item = adapter.getItem(position);
            logger.debug("DashboardItem: {} selected", item);

            DetailsFragment fragment = DetailsFragment.newInstance(item);
            Transition transition;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
              transition = new Slide();
            } else {
              transition = null;
            }

            FragmentHelper.replaceFragment(getActivity().getSupportFragmentManager(),
                fragment, transition, false);

          }
        });

    loadSensors();
  }

  private void loadSensors() {
    dataService.requestSensors(new SensorsCallback(this, dataService));
  }

  @Override
  public void onResume() {
    super.onResume();
    setupToolbar();
  }

  private void setupToolbar() {
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar == null) {
      return;
    }
    actionBar.show();
    actionBar.setTitle(R.string.dashboard);
    actionBar.setDisplayHomeAsUpEnabled(false);
    actionBar.setHomeButtonEnabled(true);
    actionBar.setDisplayUseLogoEnabled(true);

    setHasOptionsMenu(true);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_dashboard, menu);
  }

  @OnClick(R.id.fab)
  @TargetApi(21)
  void onFabClicked() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      FragmentHelper.replaceFragment(getFragmentManager(), HistoryFragment.newInstance(),
          new Slide(), false);
    } else {
      FragmentHelper.replaceFragment(getFragmentManager(), HistoryFragment.newInstance());
    }
  }

  private static class SensorsCallback implements DataService.DataCallback<List<Sensor>> {

    final WeakReference<DashboardFragment> fragment;
    final DataService service;

    public SensorsCallback(DashboardFragment fragment, DataService service) {
      super();
      this.fragment = new WeakReference<>(fragment);
      this.service = service;
    }

    @Override
    public void onSuccess(DataService service, List<Sensor> result) {
      DashboardFragment f = fragment.get();
      if (f != null) {
        List<DashboardItem> list = new ArrayList<>();
        for (Sensor s : result) {
          final Measurement m = service.getLastMeasurementForSensor(s);
          list.add(new DashboardItem(s, m));
        }
        f.adapter.addAll(list);
        f.adapter.notifyDataSetChanged();
      }
    }

    @Override
    public void onFailure(DataService service, Throwable t) {
      DashboardFragment f = fragment.get();
      if (f != null) {
        Toast.makeText(f.getContext(),
            String.format("Requesting sensors failed! {}", t.getMessage()),
            Toast.LENGTH_LONG).show();
      }
    }
  }
}
