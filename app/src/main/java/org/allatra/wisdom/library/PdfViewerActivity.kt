package org.allatra.wisdom.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.allatra.wisdom.library.db.DatabaseHandler
import org.allatra.wisdom.library.view.PdfViewerOnPageChangeListener
import android.content.Intent
import org.allatra.wisdom.library.view.PdfViewerOnTapListener

class PdfViewerActivity : AppCompatActivity() {

    private lateinit var pdfViewerOnPageChangeListener: PdfViewerOnPageChangeListener
    private lateinit var pdfViewerOnTapListener: PdfViewerOnTapListener
    private lateinit var databaseHandler: DatabaseHandler
    private var pdfId: Long = 0

    companion object {
        private const val TAG = "PdfViewerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        //val folioReader = FolioReader.get()
        //folioReader.openBook(R.raw.sensei_1_book_en)

//        databaseHandler = DatabaseHandler(this)
//
//        // Get the components
//        val pdfViewerComponent = findViewById<PDFView>(R.id.pdfView)
//        val floatingActionButtonBack = findViewById<FloatingActionButton>(R.id.floatingActionButtonBack)
//        val floatingActionButtonShare = findViewById<FloatingActionButton>(R.id.floatingActionButtonShare)
//
//        // Hide at the beginning
//        floatingActionButtonBack.hide()
//        floatingActionButtonShare.hide()
//
//        pdfViewerComponent?.let { pdfViewerComponentInner ->
//            val intent = intent
//            val indexPage = intent.getIntExtra(INDEX_PAGE, 0)
//            pdfId = intent.getLongExtra(PDF_BOOK_ID, 0)
//            val pdfBookId = intent.getIntExtra(PDF_BOOK_RES_ID, 0)
//
//            pdfViewerOnPageChangeListener = PdfViewerOnPageChangeListener(databaseHandler, pdfId)
//            floatingActionButtonBack.setOnClickListener(BackButtonOnClickListener(pdfId))
//            pdfViewerOnTapListener = PdfViewerOnTapListener(arrayOf(floatingActionButtonBack, floatingActionButtonShare))
//
//            Log.d(TAG, "Index of a page: $indexPage, BookId: $pdfId, BookResId: $pdfBookId")
//            val inputStream = resources.openRawResource(pdfBookId)
//
//            pdfViewerComponentInner.
//                fromStream(inputStream)
//                //.enableSwipe(false)
//                .swipeHorizontal(true)
//                .onPageChange(pdfViewerOnPageChangeListener)
//                .onTap(pdfViewerOnTapListener)
//                .defaultPage(indexPage)
//                .load()
//        }
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
