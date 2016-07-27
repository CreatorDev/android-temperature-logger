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

package com.imgtec.creator.petunia.presentation.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.imgtec.creator.petunia.R;

public class RecyclerItemClickSupport {

  private final RecyclerView recyclerView;

  private OnItemClickListener onItemClickListener;
  private OnItemLongClickListener onItemLongClickListener;

  private final View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      if (onItemClickListener != null) {
        RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
        onItemClickListener.onItemClicked(recyclerView, holder.getAdapterPosition(), view);
      }
    }
  };

  private final View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View view) {
      if (onItemLongClickListener != null) {
        RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
        return onItemLongClickListener.onItemLongClicked(recyclerView, holder.getAdapterPosition(), view);
      }
      return false;
    }
  };

  private final RecyclerView.OnChildAttachStateChangeListener attachListener =
      new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(View view) {
          if (onItemClickListener != null) {
            view.setOnClickListener(onClickListener);
          }
          if (onItemLongClickListener != null) {
            view.setOnLongClickListener(onLongClickListener);
          }
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {

        }
      };

  private RecyclerItemClickSupport(RecyclerView recyclerView) {
    this.recyclerView = recyclerView;
    this.recyclerView.setTag(R.id.item_click_support, this);
    this.recyclerView.addOnChildAttachStateChangeListener(attachListener);
  }

  /**
   * Adds click listener support to a specified RecyclerView.
   *
   * @param view RecyclerView instance
   * @return RecyclerItemClickSupport instance
   */
  public static RecyclerItemClickSupport addTo(RecyclerView view) {
    RecyclerItemClickSupport support = (RecyclerItemClickSupport) view.getTag(R.id.item_click_support);
    if (support == null) {
      support = new RecyclerItemClickSupport(view);
    }
    return support;
  }

  /**
   * Removes previously added click listener support from a specified RecyclerView. If no click listener support was
   * added before then this method does nothing.
   *
   * @param view RecyclerView instance
   * @return RecyclerItemClickSupport instance or null
   */
  public static RecyclerItemClickSupport removeFrom(RecyclerView view) {
    RecyclerItemClickSupport support = (RecyclerItemClickSupport) view.getTag(R.id.item_click_support);
    if (support != null) {
      support.detach(view);
    }
    return support;
  }

  public RecyclerItemClickSupport setOnItemClickListener(OnItemClickListener listener) {
    onItemClickListener = listener;
    return this;
  }

  public RecyclerItemClickSupport setOnItemLongClickListener(OnItemLongClickListener listener) {
    onItemLongClickListener = listener;
    return this;
  }

  private void detach(RecyclerView view) {
    view.removeOnChildAttachStateChangeListener(attachListener);
    view.setTag(R.id.item_click_support, null);
  }

  /**
   * Interface definition for a callback to be invoked when a recycler list item is clicked.
   */
  public interface OnItemClickListener {

    void onItemClicked(RecyclerView recyclerView, int position, View view);
  }

  /**
   * Interface definition for a callback to be invoked when long click is performed on a recycler list item.
   */
  public interface OnItemLongClickListener {

    boolean onItemLongClicked(RecyclerView recyclerView, int position, View view);
  }
}