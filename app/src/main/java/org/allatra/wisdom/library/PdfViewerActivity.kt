package org.allatra.wisdom.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.barteksc.pdfviewer.PDFView
import org.allatra.wisdom.library.db.DatabaseHandler
import org.allatra.wisdom.library.static.StaticDefinition.INDEX_PAGE
import org.allatra.wisdom.library.static.StaticDefinition.PDF_BOOK_ID
import org.allatra.wisdom.library.static.StaticDefinition.PDF_BOOK_RES_ID
import org.allatra.wisdom.library.view.PdfViewerPageChangeListener
import android.content.Intent



class PdfViewerActivity : AppCompatActivity() {

    private lateinit var pdfViewerPageChangeListener: PdfViewerPageChangeListener
    private lateinit var databaseHandler: DatabaseHandler
    private var pdfId: Long = 0

    companion object {
        private const val TAG = "PdfViewerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)
        databaseHandler = DatabaseHandler(this)

        val pdfViewerComponent = findViewById<PDFView>(R.id.pdfView)

        pdfViewerComponent?.let { pdfViewerComponentInner ->
            val intent = intent
            val indexPage = intent.getIntExtra(INDEX_PAGE, 0)
            pdfId = intent.getLongExtra(PDF_BOOK_ID, 0)
            val pdfBookId = intent.getIntExtra(PDF_BOOK_RES_ID, 0)

            pdfViewerPageChangeListener = PdfViewerPageChangeListener(databaseHandler, pdfId)

            Log.d(TAG, "Index of a page: $indexPage, BookId: $pdfId, BookResId: $pdfBookId")
            val inputStream = resources.openRawResource(pdfBookId)

            pdfViewerComponentInner.
                fromStream(inputStream)
                //.enableSwipe(false)
                .swipeHorizontal(true)
                .onPageChange(pdfViewerPageChangeListener)
                .defaultPage(indexPage)
                .load()
        }
    }

    /**
     * Called on Back button press.
     */
    override fun onBackPressed() {
        Log.d(TAG, "Back button pressed. Closing.")
        val output = Intent()
        output.putExtra("pdfId", pdfId)
        setResult(RESULT_OK, output)
        super.onBackPressed()
    }
}
