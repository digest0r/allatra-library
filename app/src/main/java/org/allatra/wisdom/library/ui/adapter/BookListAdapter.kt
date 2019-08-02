package org.allatra.wisdom.library.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.allatra.wisdom.library.R
import org.allatra.wisdom.library.db.BookInfo
import org.allatra.wisdom.library.ui.view.BookListViewHolder

class BookListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfBooks = mutableListOf<BookInfo>()

    companion object {
        private const val TAG = "BookListAdapter"
        private const val REQUEST_CODE = 100
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BookListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.book_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = listOfBooks.size

    /**
     * Method responsible for loading data when user scroll.
     */
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val bookViewHolder = viewHolder as BookListViewHolder
        bookViewHolder.bindView(listOfBooks[position])

        // This attaches listener to see where user clicked
        bookViewHolder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                Log.d(TAG, "Position clicked: $position, Book to open: ${listOfBooks[position].getTitle()}")
                val context = bookViewHolder.itemView.context


//                val reader = BufferedReader(
//                    InputStreamReader(context.assets.open("sensei_1_book_en.epub"))
//                )
//                val i = 6

                //
                //
                //folioReader.openBook(R.raw.sensei_1_book_en)
                //folioReader.openBook("file://android_asset/sensei_1_book_en.epub")
                //folioReader.openBook("file:///android_asset/sensei_1_book_en.epub")




                //Create an intent to pass previous user data
//                val context = bookViewHolder.itemView.context
//                val intent = Intent(context, PdfViewerActivity::class.java)
//                intent.putExtra(INDEX_PAGE, listOfBooks[position].getIndexPage())
//                intent.putExtra(PDF_BOOK_ID, listOfBooks[position].getId())
//                intent.putExtra(PDF_BOOK_RES_ID, listOfBooks[position].getPdfBookId())
//
//                startActivityForResult(context as MainActivity, intent, REQUEST_CODE, intent.extras)
            }
        })
    }

    fun setList(listOfBooks: MutableList<BookInfo>) {
        this.listOfBooks = listOfBooks
        notifyDataSetChanged()
    }
}