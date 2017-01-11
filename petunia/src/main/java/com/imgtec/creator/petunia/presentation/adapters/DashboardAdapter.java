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

package com.imgtec.creator.petunia.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.imgtec.creator.petunia.R;
import com.imgtec.creator.petunia.presentation.fragments.dashboard.ChangeSensorDeltaListener;
import com.imgtec.creator.petunia.presentation.fragments.dashboard.DashboardFragment;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class DashboardAdapter extends BaseAdapter<DashboardItem, DashboardAdapter.ViewHolder> {

  private static final int NA = 0;
  private static final int TEMP = NA + 1;
  private final ChangeSensorDeltaListener changeDeltaListener;

  public DashboardAdapter(ChangeSensorDeltaListener listener) {
    super();
    this.changeDeltaListener = listener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item, parent, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    DashboardItem item = getItem(position);
    load(holder, item, position);
  }

  @Override
  public int getItemViewType(int position) {
    final DashboardItem item = data.get(position);
    return item.getType();
  }

  private void load(final ViewHolder holder, final DashboardItem item, final int position) {

    holder.setTitle(item.getTitle());
    holder.setValue(item.getValue());
    holder.setDelta(item.getDelta());
    holder.editDelta.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        changeDeltaListener.onChangeDeltaSelected(item);
      }
    });
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    static final DecimalFormat df = new DecimalFormat("0.#");

    @BindView(R.id.value_container) ViewGroup valueContainer;
    @BindView(R.id.value_tv) TextView valueTV;
    @BindView(R.id.unit_tv) TextView unitTV;
    @BindView(R.id.title_tv) TextView titleTV;
    @BindView(R.id.delta) TextView deltaTV;
    @BindView(R.id.edit_delta) ImageButton editDelta;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public ViewGroup getValueContainer() {
      return valueContainer;
    }

    public TextView getValueTv() {
      return valueTV;
    }

    public void setValue(float value) {
      valueTV.setText(df.format(value));
    }

    public void setTitle(String title) {
      titleTV.setText(title);
    }

    public void setUnit(String unit) {
      unitTV.setText(unit);
    }

    public void setDelta(String delta) {
      deltaTV.setText(delta);
    }
  }
}
