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

import android.content.res.TypedArray;
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
import com.imgtec.creator.petunia.presentation.utils.DateUtils;
import com.imgtec.di.HasComponent;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HistoryFragment extends BaseChartFragment {

  public static HistoryFragment newInstance() {
    return new HistoryFragment();
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_history, menu);
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
  protected void setComponent() {
    ((HasComponent<ActivityComponent>) getActivity()).getComponent().inject(this);
  }

  @Override
  protected void requestMeasurements() {
    to = new Date();
    dataService.requestMeasurements(getFrom(), getTo(),
        getIntervalBetweenMeasurementsDisplayedOnChartForTimePeriod(range),
        new MeasurementsCallback(this));
  }

  @Override
  protected void setupToolbar() {
    ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
    if (actionBar == null) {
      return;
    }

    actionBar.setTitle(R.string.title_history);
    setHasOptionsMenu(true);
  }

  @Override @WorkerThread
  protected LineData getData(final List<Sensor> sensors, final List<Measurement> measurementList) {
    try {
      ArrayList<String> xVals = new ArrayList<>();
      HashMap<String, ArrayList<Entry>> yVals = new HashMap<>();
      ArrayList<String> addedSensors = new ArrayList<>();
      for (Sensor s : sensors) {
        yVals.put(s.getId(), new ArrayList<Entry>());
      }
      if (measurementList.size() == 0) {
        return null;
      }
      String lastX = null;
      int relativeIndex = 0;
      for (int i = 0; i < measurementList.size(); ++i) {
        final Measurement m = measurementList.get(i);
        final String x = dateFormat.format(DateUtils.UTCToLocal(measurementList.get(i).getTimestamp()));
        if (!x.equals(lastX)) {
          relativeIndex++;
          lastX = x;
          xVals.add(x);
          addedSensors.clear();
        }
        if (addedSensors.contains(m.getSensorId())) {
          continue;
        }
        try {
          float y = format.parse(m.getValue()).floatValue();
          if (yVals.get(m.getSensorId()) == null)
            continue;
          yVals.get(m.getSensorId()).add(new Entry(y, relativeIndex));
          addedSensors.add(m.getSensorId());
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }

      TypedArray lineColors = getResources().obtainTypedArray(R.array.chart_colors);
      TypedArray circleColors = getResources().obtainTypedArray(R.array.chart_circle_colors);


      ArrayList<LineDataSet> dataSets = new ArrayList<>();
      int i = 0;
      for (Sensor s : sensors) {
        LineDataSet dataSet = new LineDataSet(yVals.get(s.getId()), s.getId()); //(sensor name, id)

        dataSet.setLineWidth(3f);
        dataSet.setCircleSize(3f);
        dataSet.setColor(lineColors.getColor(i % lineColors.length(), 0));
        dataSet.setCircleColor(circleColors.getColor(i % circleColors.length(), 0));
        dataSet.setDrawValues(false);
        dataSet.setDrawCubic(true);
        dataSets.add(dataSet);
        ++i;
      }

      lineColors.recycle();
      circleColors.recycle();
      return new LineData(xVals, dataSets);
    } catch (Exception e) {
      logger.error("Error while preparing data", e);
      return null;
    }
  }

  static class MeasurementsCallback<T extends BaseChartFragment>
      implements DataService.DataCallback2<List<Sensor>, List<Measurement>> {

    private WeakReference<T> fragment;

    public MeasurementsCallback(T fragment) {
      super();
      this.fragment = new WeakReference<>(fragment);
    }


    @Override
    public void onSuccess(DataService service,List<Sensor> param,  List<Measurement> result) {
      BaseChartFragment f = fragment.get();
      if (f != null) {
        final LineData lineData = f.getData(param, result);
        f.updateChart(lineData);
        f.showChartView();
      }
    }

    @Override
    public void onFailure(DataService service, List<Sensor> param, Throwable t) {
      BaseChartFragment f = fragment.get();
      if (f != null) {
        Toast.makeText(f.getContext(),
            String.format("Requesting measurements for all sensors failed!"),
            Toast.LENGTH_LONG).show();
      }
    }
  }
}
