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

package com.imgtec.creator.petunia.presentation.fragments.dashboard;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
public class DashboardFragment extends BaseFragment implements ChangeSensorDeltaListener {


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

    adapter = new DashboardAdapter(this);

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
    dataService.startPollingForSensorChanges(new SensorsCallback(this, dataService));
  }

  @Override
  public void onPause() {
    dataService.stopPolling();
    super.onPause();
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

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.clear_datastore) {

      dataService.clearAllMeasurements(new ClearAllCallback(this));
      return true;
    }
    return super.onOptionsItemSelected(item);
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

  @Override
  public void onChangeDeltaSelected(final DashboardItem item) {
    final EditText editText = new EditText(getActivity());
    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    editText.setText(item.getDelta());
    new AlertDialog.Builder(getActivity())
        .setTitle("Set new delta for sensor:" + item.getSensor().getName())
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            final String delta = editText.getText().toString();
            //TODO: verify delta

            dataService.setDelta(item.getSensor(), Float.parseFloat(delta),
                new SetDeltaCallback(DashboardFragment.this));
          }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        })
        .setView(editText)
        .show();
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
      if (f != null && f.isAdded()) {
        List<DashboardItem> list = new ArrayList<>();
        for (Sensor s : result) {
          final Measurement m = service.getLastMeasurementForSensor(s);
          list.add(new DashboardItem(s, m));
        }
        f.adapter.clear();
        f.adapter.addAll(list);
        f.adapter.notifyDataSetChanged();
      }
    }

    @Override
    public void onFailure(DataService service, Throwable t) {
      DashboardFragment f = fragment.get();
      if (f != null && f.isAdded()) {
        Toast.makeText(f.getContext(),
            String.format("Requesting sensors failed! %s", t.getMessage()),
            Toast.LENGTH_LONG).show();
      }
    }
  }

  private static class SetDeltaCallback implements  DataService.DataCallback<Sensor> {

    final WeakReference<DashboardFragment> fragment;

    public SetDeltaCallback(DashboardFragment fragment) {
      super();
      this.fragment = new WeakReference<>(fragment);
    }

    @Override
    public void onSuccess(DataService service, Sensor result) {
      DashboardFragment f = fragment.get();
      if (f != null) {
        f.adapter.notifyDataSetChanged();
      }
    }

    @Override
    public void onFailure(DataService service, Throwable t) {
      DashboardFragment f = fragment.get();
      if (f != null) {
        Toast.makeText(f.getContext(),
            String.format("Setting delta failed! %s", t.getMessage()),
            Toast.LENGTH_LONG).show();
      }
    }
  }

  private static class ClearAllCallback implements DataService.DataCallback<Void> {

    final WeakReference<DashboardFragment> fragment;

    public ClearAllCallback(DashboardFragment fragment) {
      super();
      this.fragment = new WeakReference<>(fragment);
    }

    @Override
    public void onSuccess(DataService service, Void result) {
      DashboardFragment f = fragment.get();
      if (f != null) {
        Toast.makeText(f.getContext(),
            String.format("Removing all measurements finished successfully"),
            Toast.LENGTH_LONG).show();
      }
    }

    @Override
    public void onFailure(DataService service, Throwable t) {
      DashboardFragment f = fragment.get();
      if (f != null) {
        Toast.makeText(f.getContext(),
            String.format("Clear all data failed! %s", t.getMessage()),
            Toast.LENGTH_LONG).show();
      }
    }
  }
}
