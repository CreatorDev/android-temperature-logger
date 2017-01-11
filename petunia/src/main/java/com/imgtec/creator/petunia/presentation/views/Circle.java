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
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.UiThread;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;

import com.imgtec.creator.petunia.R;

public class Circle extends View {

  int baseWidth, progressWidth = 10;
  int progress = 0;
  int baseColor = Color.BLACK;
  int progressColor = Color.BLUE;
  int textColor = Color.BLACK;
  int textSize = 10;
  boolean inverseProgressDirection = false;
  Drawable icon;
  Bitmap iconBitmap;
  String label;

  Paint paint, progressPaint, textPaint;
  RectF baseBounds, progressBounds;

  public Circle(Context context) {
    super(context);

  }

  public Circle(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public Circle(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  public void setProgress(int progress) {
    if (progress < 0 || progress > 100) {
      throw new IllegalArgumentException("Progress must in range [0..100]");
    }
    this.progress = progress;
    postInvalidate();
  }

  @UiThread
  public void setImageResource(@DrawableRes int resId) {
    icon = ResourcesCompat.getDrawable(getResources(), resId, null);
    iconBitmap = drawableToBitmap(icon);
    invalidate();
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.circle, 0, 0);
    try {
      baseWidth = a.getDimensionPixelSize(R.styleable.circle_baseWidth, 10);
      progressWidth = a.getDimensionPixelSize(R.styleable.circle_progressWidth, 10);
      progress = a.getInt(R.styleable.circle_progress, 0);
      baseColor = a.getColor(R.styleable.circle_baseColor, Color.BLACK);
      progressColor = a.getColor(R.styleable.circle_progressColor, Color.BLUE);
      textColor = a.getColor(R.styleable.circle_textColor, Color.BLACK);
      textSize = a.getDimensionPixelSize(R.styleable.circle_textSize, 10);
      label = a.getString(R.styleable.circle_label);
      icon = a.getDrawable(R.styleable.circle_src);
      inverseProgressDirection = a.getBoolean(R.styleable.circle_inverseProgressDirection, false);
      if (icon != null) {
        iconBitmap = drawableToBitmap(icon);
      }
    } finally {
      a.recycle();
    }
    baseBounds = new RectF();
    progressBounds = new RectF();
    initPaint();

  }

  public static Bitmap drawableToBitmap (Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable)drawable).getBitmap();
    }

    int width = drawable.getIntrinsicWidth();
    width = width > 0 ? width : 1;
    int height = drawable.getIntrinsicHeight();
    height = height > 0 ? height : 1;

    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);

    return bitmap;
  }

  private void initPaint() {
    paint = new Paint();
    paint.setColor(baseColor);
    paint.setStrokeWidth(baseWidth);
    paint.setStyle(Paint.Style.STROKE);
    paint.setAntiAlias(true);

    progressPaint = new Paint();
    progressPaint.setColor(progressColor);
    progressPaint.setStrokeWidth(progressWidth);
    progressPaint.setStyle(Paint.Style.STROKE);
    progressPaint.setAntiAlias(true);

    textPaint = new Paint();
    textPaint.setAntiAlias(true);
    textPaint.setTextAlign(Paint.Align.CENTER);
    textPaint.setStyle(Paint.Style.FILL);
    textPaint.setStrokeWidth(2);
    textPaint.setTextSize(textSize);

  }

  @Override
  public void draw(Canvas canvas) {
    super.draw(canvas);
    int radius = Math.min(getWidth(), getHeight())/2;
    int width = getWidth();
    int height = getHeight();
    int offsetX = (int) ((width - Math.min(width, height)) / 2f);
    int centerX = offsetX + radius;

    baseBounds.set(baseWidth/2 + offsetX, baseWidth/2,width-baseWidth/2-offsetX, 2*radius-baseWidth/2);
    progressBounds.set(progressWidth/2+baseWidth+offsetX, progressWidth/2+baseWidth, width-progressWidth/2-baseWidth-offsetX, 2*radius-progressWidth/2-baseWidth);

    canvas.drawArc(baseBounds, -220, 260, false, paint);
    if (inverseProgressDirection) {
      canvas.drawArc(progressBounds, -220f + 260f - 260f / 100 * progress,  260f/100*progress, false, progressPaint);
    } else {
      canvas.drawArc(progressBounds, -220, 260f / 100 * progress, false, progressPaint);
    }
    int y = (int) (Math.sin(50*Math.PI/180)*radius+radius-textSize/2);
    canvas.drawText(label, centerX, y, textPaint);
    if (iconBitmap != null) {
      canvas.drawBitmap(iconBitmap, centerX-iconBitmap.getWidth()/2, radius-iconBitmap.getHeight()/2, paint);
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }


}
