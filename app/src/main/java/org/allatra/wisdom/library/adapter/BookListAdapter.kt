package org.allatra.wisdom.library.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.allatra.wisdom.library.R
import org.allatra.wisdom.library.db.BookInfo
import org.allatra.wisdom.library.view.BookListViewHolder
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import org.allatra.wisdom.library.PdfViewerActivity

class BookListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfBooks = mutableListOf<BookInfo>()

    companion object {
        private const val TAG = "BookListAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BookListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false))
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

                //Create an intent to pass previous user data
                val context = bookViewHolder.itemView.context
                val intent = Intent(context, PdfViewerActivity::class.java)
                intent.putExtra("indexPage", listOfBooks[position].getIndexPage())
                intent.putExtra("pdfBookName", listOfBooks[position].getPdfName())
                startActivity(context, intent, intent.extras)
            }
        })
    }

    fun setList(listOfBooks: MutableList<BookInfo>) {
        this.listOfBooks = listOfBooks
        notifyDataSetChanged()
    }
}