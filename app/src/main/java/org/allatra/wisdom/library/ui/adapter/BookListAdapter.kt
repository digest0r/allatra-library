package org.allatra.wisdom.library.ui.adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.allatra.wisdom.library.R
import org.allatra.wisdom.library.db.BookInfo
import timber.log.Timber
import java.io.ByteArrayInputStream

class BookListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfBooks = mutableListOf<BookInfo>()

    companion object {
        private const val TAG = "BookListAdapter"
        private const val REQUEST_CODE = 100
    }

    inner class BookListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById<View>(R.id.title) as TextView
        val imageView: ImageView = itemView.findViewById(R.id.thumbnail) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BookListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.book_item2,
                parent,
                false
            )
        )
    }

    /**
     * Method responsible for loading data when user scroll.
     */
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val bookViewHolder = viewHolder as BookListViewHolder

        val book = listOfBooks[position]
        // Bind items
        viewHolder.textViewTitle.text = book.getTitle()
        viewHolder.textViewTitle.contentDescription = "\u00A0"

        if (book.getTitle()!=null) {
            viewHolder.imageView.contentDescription = book.getTitle()
        }

        book.getCover()?.let {
            val arrayInputStream = ByteArrayInputStream(it)
            val bitmap = BitmapFactory.decodeStream(arrayInputStream)
            viewHolder.imageView.setImageBitmap(bitmap)
        } ?: run {
            Timber.e("Image was not found.")
            viewHolder.imageView.setImageResource(R.drawable.allatra_ru)
//            book.coverLink?.let {
//                val baseUrl = server + "/" + book.fileName + it
//                Picasso.with(activity).load(baseUrl).into(viewHolder.imageView)
//            }
        }

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

    override fun getItemCount(): Int = listOfBooks.size
}