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


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.imgtec.creator.petunia.R;


public class ProgressButton extends FrameLayout {
  private Button button;
  private ProgressBar progressBar;

  public ProgressButton(Context context) {
    super(context);
    initialize(null);
  }

  public ProgressButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize(attrs);
  }

  public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize(attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initialize(attrs);
  }

  private void initialize(AttributeSet attrs) {
    inflate(getContext(), R.layout.view_progress_button, this);
    button = (Button)findViewById(R.id.button);
    progressBar = (ProgressBar)findViewById(R.id.progressBar);
    progressBar.getIndeterminateDrawable().setColorFilter(
        getContext().getResources().getColor(R.color.primary_color_purple), PorterDuff.Mode.SRC_IN);
    progressBar.setVisibility(View.GONE);

    if (attrs == null)
      return;

    TypedArray ta = getContext().obtainStyledAttributes(attrs,
        R.styleable.ProgressButton, 0, 0);
    String text = ta.getString(R.styleable.ProgressButton_android_text);
    button.setText(text);

    int bgResource = ta.getResourceId(R.styleable.ProgressButton_android_background, 0);
    if (bgResource != 0)
      button.setBackgroundResource(bgResource);

    ta.recycle();
  }

  public void setProgress(boolean progress) {
    progressBar.setVisibility(progress ? View.VISIBLE : View.GONE);
    button.setEnabled(!progress);
  }

  @Override
  public void setOnClickListener(OnClickListener l) {
    button.setOnClickListener(l);
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    button.setEnabled(enabled);
  }
}
