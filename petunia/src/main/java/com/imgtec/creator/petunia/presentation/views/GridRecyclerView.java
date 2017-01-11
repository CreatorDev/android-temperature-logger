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
