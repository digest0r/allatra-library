package org.allatra.wisdom.library.view

import android.view.MotionEvent
import com.github.barteksc.pdfviewer.listener.OnTapListener
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PdfViewerOnTapListener(val buttonsArray: Array<FloatingActionButton>): OnTapListener {

    override fun onTap(e: MotionEvent?): Boolean {
        buttonsArray.forEach { button ->
            if(button.isOrWillBeShown){
                button.hide()
            } else {
                button.show()
            }
        }
        return true
    }
}