package org.allatra.wisdom.library.ui.decoration

import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.util.TypedValue

class GridSpacingItemDecoration: RecyclerView.ItemDecoration {

    private var spanCount: Int = 0
    private var spacing: Int = 0
    private var includeEdge: Boolean = true
    private var context: Context? = null

    constructor(spanCount: Int, spacing: Int, includeEdge: Boolean, context: Context): this() {
        this.context = context
        this.spanCount = spanCount
        this.spacing = dpToPx(spacing)
        this.includeEdge = includeEdge
    }

    constructor()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
            outRect.right =
                spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private fun dpToPx(dp: Int): Int {
        val r = context!!.resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }
}