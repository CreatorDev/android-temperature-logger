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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.GridLayoutAnimationController;

public class GridRecyclerView extends RecyclerView{

  public GridRecyclerView(Context context) {
    super(context);
  }

  public GridRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public GridRecyclerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  public void setLayoutManager(LayoutManager layout) {
    if (layout instanceof GridLayoutManager){
      super.setLayoutManager(layout);
    } else {
      throw new ClassCastException("You should only use a GridLayoutManager with GridRecyclerView.");
    }
  }

  @Override
  protected void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {

    if (getAdapter() != null && getLayoutManager() instanceof GridLayoutManager){

      GridLayoutAnimationController.AnimationParameters animationParams =
          (GridLayoutAnimationController.AnimationParameters) params.layoutAnimationParameters;

      if (animationParams == null) {
        animationParams = new GridLayoutAnimationController.AnimationParameters();
        params.layoutAnimationParameters = animationParams;
      }

      int columns = ((GridLayoutManager) getLayoutManager()).getSpanCount();

      animationParams.count = count;
      animationParams.index = index;
      animationParams.columnsCount = columns;
      animationParams.rowsCount = count / columns;

      final int invertedIndex = count - 1 - index;
      animationParams.column = columns - 1 - (invertedIndex % columns);
      animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns;

    } else {
      super.attachLayoutAnimationParameters(child, params, index, count);
    }
  }
}
