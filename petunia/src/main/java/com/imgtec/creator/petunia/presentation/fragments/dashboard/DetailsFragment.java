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


import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.imgtec.creator.petunia.R;
import com.imgtec.creator.petunia.data.DataService;
import com.imgtec.creator.petunia.data.Measurement;
import com.imgtec.creator.petunia.data.Sensor;
import com.imgtec.creator.petunia.presentation.ActivityComponent;
import com.imgtec.creator.petunia.presentation.adapters.DashboardItem;
import com.imgtec.di.HasComponent;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class DetailsFragment extends BaseChartFragment {

  private DashboardItem item;

  public static DetailsFragment newInstance(DashboardItem item) {
    DetailsFragment fragment = new DetailsFragment();
    Bundle bundle = new Bundle();
    bundle.putSerializable("DashboardItem", item);
    fragment.setArguments(bundle);

    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      item = (DashboardItem) getArguments().getSerializable("DashboardItem");
    }
    symbols.setDecimalSeparator('.');
    format.setDecimalFormatSymbols(symbols);
  }

  @Override
  protected void requestMeasurements() {
    to = new Date();
    dataService.requestMeasurements(item.getSensor(),
        getFrom(), getTo(), getIntervalBetweenMeasurementsDisplayedOnChartForTimePeriod(range),
        new MeasurementsCallback(this));
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_details, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.choose_date) {
      final DialogFragment dialog = new ChooseDateDialog();
      dialog.setTargetFragment(this, 0);
      dialog.show(getActivity().getSupportFragmentManager(), "choose_date");
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void setupToolbar() {
    ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
    if (actionBar == null) {
      return;
    }

    actionBar.show();
    actionBar.setDisplayHomeAsUpEnabled(false);
    actionBar.setHomeButtonEnabled(true);
    actionBar.setTitle(item.getTitle());
    setHasOptionsMenu(true);
  }

  @Override
  protected void setComponent() {
    ((HasComponent<ActivityComponent>) getActivity()).getComponent().inject(this);
  }

  @Override @WorkerThread
  protected LineData getData(final List<Sensor> sensors, final List<Measurement> measurementList) {

    if (measurementList.size() == 0) {
      return null;
    }
    ArrayList<String> xVals = new ArrayList<>();
    ArrayList<Entry> yVals = new ArrayList<>();
    for (int i = 0; i < measurementList.size(); ++i) {
      final Measurement m = measurementList.get(i);
      final String x = dateFormat.format(m.getTimestamp());
      xVals.add(x);
      try {
        float y = format.parse(m.getValue()).floatValue();
        yVals.add(new Entry(y, i));
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    LineDataSet dataSet = new LineDataSet(yVals, item.getTitle());

    dataSet.setLineWidth(3f);
    dataSet.setCircleSize(3f);
    dataSet.setColor(getResources().getColor(R.color.supporting_color_light_purple));
    dataSet.setCircleColor(getResources().getColor(R.color.supporting_color_dark_purple));
    dataSet.setHighLightColor(getResources().getColor(R.color.supporting_color_light_purple));
    dataSet.setDrawValues(false);
    dataSet.setDrawCubic(true);


    ArrayList<LineDataSet> dataSets = new ArrayList<>();
    dataSets.add(dataSet);

    return new LineData(xVals, dataSets);
  }

  static class MeasurementsCallback<T extends BaseChartFragment> implements DataService.DataCallback2<Sensor, List<Measurement>> {

    private WeakReference<T> fragment;

    public MeasurementsCallback(T fragment) {
      super();
      this.fragment = new WeakReference<>(fragment);
    }

    @Override
    public void onSuccess(DataService service, Sensor param, List<Measurement> result) {

      BaseChartFragment f = fragment.get();
      if (f != null) {
        final LineData lineData = f.getData(new ArrayList<>(Arrays.asList(param)), result);
        f.updateChart(lineData);
        f.showChartView();
      }
    }

    @Override
    public void onFailure(DataService service, Sensor param, Throwable t) {
      BaseChartFragment f = fragment.get();
      if (f != null) {
        Toast.makeText(f.getContext(),
            String.format("Requesting measurements for sensors %s failed!", param.getId()),
            Toast.LENGTH_LONG).show();
      }
    }
  }
}
