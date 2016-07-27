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

package com.imgtec.creator.petunia.presentation.views;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

  private int space;

  public SpaceItemDecoration(int space) {
    this.space = space;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

    GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
    GridLayoutManager.LayoutParams layoutParams
        = (GridLayoutManager.LayoutParams) view.getLayoutParams();

    outRect.left = layoutParams.getSpanIndex() == 0 ? space : space / 2;
    outRect.right = layoutParams.getSpanIndex() == layoutManager.getSpanCount() - 1 ? space : space / 2;
    outRect.top = layoutParams.getViewLayoutPosition() < layoutManager.getSpanCount() ? space : 0;
    outRect.bottom = space;
  }
}
