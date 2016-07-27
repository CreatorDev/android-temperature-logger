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
