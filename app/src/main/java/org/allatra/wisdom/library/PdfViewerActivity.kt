package org.allatra.wisdom.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.barteksc.pdfviewer.PDFView
import org.allatra.wisdom.library.static.StaticDefinition.INDEX_PAGE
import org.allatra.wisdom.library.static.StaticDefinition.PDF_BOOK_ID

class PdfViewerActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "PdfViewerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        val pdfViewerComponent = findViewById<PDFView>(R.id.pdfView)

        pdfViewerComponent?.let { pdfViewerComponentInner ->
            val intent = intent
            val indexPage = intent.getIntExtra(INDEX_PAGE, 0)
            val pdfBookId = intent.getIntExtra(PDF_BOOK_ID, 0)

            val inputStream = resources.openRawResource(pdfBookId)
            pdfViewerComponentInner.fromStream(inputStream).load()
        }
    }
}
