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

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseAdapter<V, K extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<K> {

  protected List<V> data = new ArrayList<>();

  @Override
  public abstract K onCreateViewHolder(ViewGroup parent, int viewType);

  @Override
  public abstract void onBindViewHolder(K holder, int position);

  @Override
  public int getItemCount() {
    return data.size();
  }

  public void add(@NonNull V item) {
    if (!data.contains(item)) {
      data.add(item);
    }
  }

  public void addAll(@NonNull Collection<V> items) {
    for (V item : items) {
      add(item);
    }
  }

  public int getPosition(V item) {
    return data.indexOf(item);
  }

  public V getItem(int position) {
    return data.get(position);
  }

  public List<V> getItems() {
    return new ArrayList<>(data);
  }

  public void remove(int position) {
    data.remove(position);
  }

  public void clear() {
    data.clear();
  }
}