package org.allatra.wisdom.library.view

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.allatra.wisdom.library.PdfViewerActivity

class BackButtonOnClickListener(private val pdfId: Long): View.OnClickListener {

    companion object {
        private const val TAG = "BackButtonOnClick"
    }

    override fun onClick(view: View?) {
        view?.let {
            Log.d(TAG, "Navigation: Back button pressed. Closing.")
            val output = Intent()
            output.putExtra("pdfId", pdfId)
            val activity = view.context as PdfViewerActivity
            activity.setResult(AppCompatActivity.RESULT_OK, output)
            activity.finish()
        }
    }
}