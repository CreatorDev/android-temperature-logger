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

package com.imgtec.creator.petunia.presentation.utils;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.imgtec.creator.petunia.R;

import javax.inject.Inject;

/**
 *
 */
public class ToolbarHelper {

  private final Activity activity;
  private Toolbar toolbar;
  private ProgressBar progressBar;

  @Inject public ToolbarHelper(Activity activity) {
    this.activity = activity;
  }

  public Toolbar getToolbar() {
    if (toolbar == null) {
      throw new IllegalArgumentException("Set toolbar first.");
    }
    return toolbar;
  }

  public void setToolbar(Toolbar toolbar) {
    this.toolbar = toolbar;
  }

  public void showProgress() {
    activity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (progressBar == null) {
          progressBar = (ProgressBar) toolbar.findViewById(R.id.progress);
        }
        progressBar.setVisibility(View.VISIBLE);
      }
    });
  }

  public void hideProgress() {
    activity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (progressBar == null) {
          progressBar = (ProgressBar) toolbar.findViewById(R.id.progress);
        }
        progressBar.setVisibility(View.GONE);
      }
    });
  }
}
