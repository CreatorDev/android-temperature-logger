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
        float y = format.parse(String.valueOf(m.getValue())).floatValue();
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
            String.format("Requesting measurements for sensors %s failed!" + t.getMessage(),
                param.getId()),  Toast.LENGTH_LONG).show();
      }
    }
  }
}
