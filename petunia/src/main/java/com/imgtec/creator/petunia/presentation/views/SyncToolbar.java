/*
 * <b>Copyright 2015 by Imagination Technologies Limited
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

import android.content.Context;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imgtec.creator.petunia.R;

import butterknife.BindView;
import butterknife.ButterKnife;

@UiThread
public class SyncToolbar extends RelativeLayout {

  public static final int MODE_INFO = 1;
  public static final int MODE_PROGRESS = 2;

  @BindView(R.id.offline_icon_iv) ImageView offlineIndicator;
  @BindView(R.id.info_tv) TextView infoTv;
  @BindView(R.id.sync_info_tv) TextView syncInfoTv;
  @BindView(R.id.sub_info_tv) TextView subscriptionInfoTv;
  @BindView(R.id.sync_toolbar_progressbar) ProgressBar progressBar;


  public SyncToolbar(Context context) {
    super(context);
    initUI(context);
  }

  public SyncToolbar(Context context, AttributeSet attrs) {
    super(context, attrs);
    initUI(context);
  }

  public SyncToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initUI(context);
  }

  private void initUI(Context context) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    inflater.inflate(R.layout.sync_toolbar, this, true);

    ButterKnife.bind(this);
  }

  public void showIcon() {
    offlineIndicator.setVisibility(View.VISIBLE);
  }

  public void hideIcon() {
    offlineIndicator.setVisibility(View.INVISIBLE);
  }

  public void setInfoText(String infoText) {
    infoTv.setText(infoText);
  }

  public void setSyncText(String syncText) {
    syncInfoTv.setText(syncText);
  }


  public void setSubscriptionText(String syncText) {
    subscriptionInfoTv.setText(syncText);
  }

  public void showSubscriptionText() {
    subscriptionInfoTv.setVisibility(VISIBLE);
  }

  public void hideSubscriptionText() {
    subscriptionInfoTv.setVisibility(GONE);
  }

  public void setMode(int mode) {
    if (mode == MODE_PROGRESS) {
      syncInfoTv.setVisibility(View.VISIBLE);
      progressBar.setVisibility(View.VISIBLE);
      infoTv.setVisibility(View.GONE);
      subscriptionInfoTv.setVisibility(GONE);
    } else {
      syncInfoTv.setVisibility(View.GONE);
      progressBar.setVisibility(View.GONE);
      infoTv.setVisibility(View.VISIBLE);
      subscriptionInfoTv.setVisibility(GONE);
    }
  }


}
