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