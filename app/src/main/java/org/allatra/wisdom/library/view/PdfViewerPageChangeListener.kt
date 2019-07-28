package org.allatra.wisdom.library.view

import android.util.Log
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import org.allatra.wisdom.library.db.BookInfo
import org.allatra.wisdom.library.db.DatabaseHandler

class PdfViewerPageChangeListener(val databaseHandler: DatabaseHandler,
                                  var bookId: Long
): OnPageChangeListener{

    private var bookInfo: BookInfo? = null

    companion object {
        private const val TAG = "PdfVPChangeListener"
    }

    override fun onPageChanged(page: Int, pageCount: Int){
        Log.d(TAG, "Page changed to: $page, total pages: $pageCount")
        if(bookInfo == null) {
            bookInfo = databaseHandler.getBookInfoById(bookId)
            if(bookInfo == null){
                Log.e(TAG, "BookInfo returned null for id=$bookInfo")
            }
        } else {
            updateRecord(page)
        }
    }

    private fun updateRecord(page: Int){
        bookInfo?.let{
            //it.setIndexPage(page)
            databaseHandler.updateBookInfo(it, page)
        }?: run {

        }
    }
}