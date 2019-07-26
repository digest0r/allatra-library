package org.allatra.wisdom.library.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Adding space to every row populated.
 */
class VerticalSpaceItemDecoration (private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        outRect.bottom = verticalSpaceHeight
    }
}