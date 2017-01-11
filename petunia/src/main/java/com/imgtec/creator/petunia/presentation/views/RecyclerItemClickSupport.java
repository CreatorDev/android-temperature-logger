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