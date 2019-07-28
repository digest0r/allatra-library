package org.allatra.wisdom.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.barteksc.pdfviewer.PDFView
import org.allatra.wisdom.library.R

class PdfViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        val pdfViewerComponent = findViewById<PDFView>(R.id.pdfView)
        pdfViewerComponent?.let {
            val inputStream = resources.openRawResource(R.raw.sensei_1_book_pdf_en)
            it.fromStream(inputStream).load()
        }
    }
}
