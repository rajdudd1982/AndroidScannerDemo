package com.scanner.demo.ui.base

import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(val orientation: Int = LinearLayout.VERTICAL, val space: Int = 0) : RecyclerView.ItemDecoration() {

    companion object {
        val HORIZONTAL = LinearLayout.HORIZONTAL
        val VERTICAL = LinearLayout.VERTICAL
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (VERTICAL == orientation){
            outRect.top = space
            outRect.bottom = space
        } else {
            outRect.left = space
            outRect.right = space
        }

    }
}