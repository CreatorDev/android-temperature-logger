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
