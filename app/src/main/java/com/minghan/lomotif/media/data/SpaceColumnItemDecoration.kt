package com.minghan.lomotif.media.data

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceColumnItemDecoration(
    private val spanCount: Int,
    private val space: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val index = parent.getChildLayoutPosition(view)
        val column = index % spanCount

        outRect.left = column * space / spanCount
        outRect.right = space - (column + 1) * space / spanCount
        if (index >= spanCount) {
            outRect.top = space
        }
    }
}
