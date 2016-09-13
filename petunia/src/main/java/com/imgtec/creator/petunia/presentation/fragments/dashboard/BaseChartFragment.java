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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.imgtec.creator.petunia.R;
import com.imgtec.creator.petunia.data.DataService;
import com.imgtec.creator.petunia.data.Measurement;
import com.imgtec.creator.petunia.data.Sensor;
import com.imgtec.creator.petunia.presentation.fragments.BaseFragment;
import com.imgtec.creator.petunia.presentation.utils.ToolbarHelper;

import org.slf4j.Logger;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;

interface DateSetListener {
  void onDateSet(BaseChartFragment.TimePeriod dateRange);
}

public abstract class BaseChartFragment extends BaseFragment implements
    DateSetListener {

  enum TimePeriod {
    HOUR(R.string.last_hour, 1000 * 60 * 60), HOUR_12(R.string.last_12_hours, 1000 * 60 * 60 * 12),
    DAY(R.string.last_day, 1000 * 60 * 60 * 24), WEEK(R.string.last_weak, 1000 * 60 * 60 * 24 * 7);

    int textId;
    long millis;

    TimePeriod(int textId, long milis) {
      this.textId= textId;
      this.millis = milis;
    }

    long getInMillis() {
      return millis;
    }


    public String toLocalizedString(Resources res) {
      return res.getString(textId);
    }
  }

  @Inject Logger logger;
  @Inject DataService dataService;
  @Inject ToolbarHelper toolbarHelper;
  @Inject @Named("Main") Handler handler;

  @BindView(R.id.chart) LineChart chart;
  @BindView(R.id.chart_progressbar) ProgressBar progressBar;
  @BindView(R.id.chart_cache) ImageView chartCacheIV;

  protected SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d kk:mm:ss");

  protected DecimalFormatSymbols symbols = new DecimalFormatSymbols();
  protected DecimalFormat format = new DecimalFormat("0.#");

  protected Date from;
  protected Date to;
  protected TimePeriod range;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    symbols.setDecimalSeparator('.');
    format.setDecimalFormatSymbols(symbols);
    setRetainInstance(true);
    range = TimePeriod.HOUR;
    to = new Date();
    from = new Date(getTo().getTime() - (TimePeriod.HOUR.getInMillis()));
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_details, container, false);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    setupToolbar();
    setupChart();
  }

  @Override
  public void onResume() {
    super.onResume();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        from = getFrom();
        to = getTo();
        requestMeasurements();
      }
    }, 1000);

    if (chart.getLineData() == null) {
      showProgressBar();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    hideChartView();
  }

  @UiThread
  protected void showProgressBar() {
    if (chart == null) {
      return;
    }
    chart.setVisibility(View.INVISIBLE);
    chartCacheIV.setVisibility(View.INVISIBLE);
    progressBar.setVisibility(View.VISIBLE);
  }

  @UiThread
  protected void showChartView() {
    if (chart == null) {
      return;
    }
    chart.setVisibility(View.VISIBLE);
    chartCacheIV.setVisibility(View.INVISIBLE);
    progressBar.setVisibility(View.INVISIBLE);
  }

  @UiThread
  protected void hideChartView() {
    if (chart == null) {
      return;
    }
    chartCacheIV.setImageBitmap(chart.getChartBitmap());
    chartCacheIV.setVisibility(View.VISIBLE);
    chart.setVisibility(View.INVISIBLE);
    progressBar.setVisibility(View.INVISIBLE);
  }

  @UiThread
  protected void setupChart() {
    chart.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
    chart.setGridBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
    chart.getPaint(Chart.PAINT_INFO).setColor(ContextCompat.getColor(getContext(),
        R.color.supporting_color_light_purple));
    chart.getXAxis().setLabelRotationAngle(-45f);
    chart.getXAxis().setPosition(XAxis.XAxisPosition.TOP);
    chart.getXAxis().setYOffset(0);
    chart.getXAxis().setAvoidFirstLastClipping(true);
    chart.setDescription("");
  }

  @UiThread
  protected void updateChart(LineData lineData) {
    if (chart != null) {
      if (lineData == null) {
        chart.clear();
      }
      else {
        chart.setData(lineData);
        chart.invalidate();
      }
    }
  }

  protected abstract LineData getData(final List<Sensor> sensors,
                                      final List<Measurement> measurementList);

  protected abstract void requestMeasurements();

  protected abstract void setupToolbar();

  protected Date getFrom() {
    return from;
  }

  protected Date getTo() {
    return to;
  }

  @Override
  public void onDateSet(TimePeriod dateRange) {
    from = new Date(to.getTime() - dateRange.getInMillis());
    range = dateRange;
    showProgressBar();
    requestMeasurements();
  }

  public static class ChooseDateDialog extends DialogFragment {

    private String[] items;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);

      final Resources res = getResources();
      items = new String[] {
          TimePeriod.HOUR.toLocalizedString(res),
          TimePeriod.HOUR_12.toLocalizedString(res),
          TimePeriod.DAY.toLocalizedString(res),
          TimePeriod.WEEK.toLocalizedString(res)
      };
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
      builder.setTitle(R.string.choose_time_range);
      builder.setItems(items, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          ((DateSetListener) getTargetFragment()).onDateSet(TimePeriod.values()[which]);
          dismiss();
        }
      });
      return builder.create();
    }
  }

  long getIntervalBetweenMeasurementsDisplayedOnChartForTimePeriod(TimePeriod range) {
    switch (range) {
      case HOUR:
        return 60; // 1 min
      case HOUR_12:
        return 60 * 10; // 10 mins
      case DAY:
        return 60 * 15; // 15 mins
      case WEEK:
        return 60 * 60 * 2; // 2 hours
      default:
        return 60;
    }
  }
}
