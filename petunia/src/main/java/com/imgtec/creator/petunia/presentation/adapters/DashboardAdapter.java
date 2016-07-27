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

package com.imgtec.creator.petunia.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imgtec.creator.petunia.R;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class DashboardAdapter extends BaseAdapter<DashboardItem, DashboardAdapter.ViewHolder> {

  private static final int NA = 0;
  private static final int TEMP = NA + 1;

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

  private void load(final ViewHolder holder, DashboardItem item, final int position) {

    holder.setTitle(item.getTitle());
    holder.setValue(item.getValue());
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    static final DecimalFormat df = new DecimalFormat("0.#");

    @BindView(R.id.value_container) ViewGroup valueContainer;
    @BindView(R.id.value_tv) TextView valueTV;
    @BindView(R.id.unit_tv) TextView unitTV;
    @BindView(R.id.title_tv) TextView titleTV;

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

    public void setValue(String value) {
      valueTV.setText(df.format(Float.parseFloat(value)));
    }

    public void setTitle(String title) {
      titleTV.setText(title);
    }

    public void setUnit(String unit) {
      unitTV.setText(unit);
    }
  }
}
