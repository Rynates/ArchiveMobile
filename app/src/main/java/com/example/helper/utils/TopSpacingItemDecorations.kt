package com.example.helper.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class TopSpacingItemDecorations( private val left: Int = 0,
                                 private val top: Int = 0,
                                 private val right: Int = 0,
                                 private val bottom: Int = 0): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(left,top,right,bottom)
        //parent.itemAnimator = ItemAnimator()
    }
}