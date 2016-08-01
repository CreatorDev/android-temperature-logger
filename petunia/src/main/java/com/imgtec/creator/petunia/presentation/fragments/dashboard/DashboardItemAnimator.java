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

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imgtec.creator.petunia.presentation.adapters.DashboardAdapter;

import java.util.List;

/**
 *
 */
public class DashboardItemAnimator extends DefaultItemAnimator {

  private static final String TAG = "DashboardItemAnimator";

  @Override
  public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull final RecyclerView.ViewHolder newHolder, @NonNull ItemHolderInfo preInfo, @NonNull final ItemHolderInfo postInfo) {

    final TextView valueTV = ((DashboardAdapter.ViewHolder)newHolder).getValueTv();
    final ViewGroup valueContainer = ((DashboardAdapter.ViewHolder)newHolder).getValueContainer();
    valueTV.setText(((DashboardItemHolderInfo)preInfo).value);
    ObjectAnimator animator1 = ObjectAnimator.ofFloat(valueContainer, View.ROTATION_X, 0, 90);
    ObjectAnimator animator2 = ObjectAnimator.ofFloat(valueContainer, View.ROTATION_X, -90, 0);
    animator1.setDuration(300);
    animator2.setDuration(300);
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playSequentially(animator1, animator2);
    animator1.addListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animation) {

      }

      @Override
      public void onAnimationEnd(Animator animation) {
        valueTV.setText(((DashboardItemHolderInfo)postInfo).value);
      }

      @Override
      public void onAnimationCancel(Animator animation) {

      }

      @Override
      public void onAnimationRepeat(Animator animation) {

      }
    });
    animatorSet.addListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animation) {

      }

      @Override
      public void onAnimationEnd(Animator animation) {
        dispatchChangeFinished(newHolder, true);
      }

      @Override
      public void onAnimationCancel(Animator animation) {

      }

      @Override
      public void onAnimationRepeat(Animator animation) {

      }
    });
    animatorSet.start();
    return true;
  }

  @Override
  public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
    return true;
  }

  @NonNull
  @Override
  public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state,
                                                   @NonNull RecyclerView.ViewHolder viewHolder,
                                                   int changeFlags, @NonNull List<Object> payloads) {

    DashboardItemHolderInfo dashboardItemHolderInfo = new DashboardItemHolderInfo();
    dashboardItemHolderInfo.value = ((DashboardAdapter.ViewHolder) viewHolder).getValueTv().getText().toString();
    return dashboardItemHolderInfo;
  }

  @NonNull
  @Override
  public ItemHolderInfo recordPostLayoutInformation(@NonNull RecyclerView.State state,
                                                    @NonNull RecyclerView.ViewHolder viewHolder) {

    DashboardItemHolderInfo dashboardItemHolderInfo = new DashboardItemHolderInfo();
    dashboardItemHolderInfo.value = ((DashboardAdapter.ViewHolder) viewHolder).getValueTv().getText().toString();
    return dashboardItemHolderInfo;
  }

  class DashboardItemHolderInfo extends ItemHolderInfo {
    String value;

    @Override
    public String toString() {
      return "DashboardItemHolderInfo{" +
          "value='" + value + '\'' +
          '}';
    }
  }
}