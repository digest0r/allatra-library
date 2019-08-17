package org.allatra.wisdom.library.ui.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.allatra.wisdom.library.R
import org.allatra.wisdom.library.db.BookInfo
import timber.log.Timber
import java.io.ByteArrayInputStream
import android.widget.LinearLayout
import android.view.WindowManager
import android.view.Gravity
import android.widget.ProgressBar
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import nl.komponents.kovenant.task
import nl.komponents.kovenant.then
import org.allatra.wisdom.library.ui.activity.MainActivity
import org.allatra.wisdom.library.ui.activity.R2ReaderActivity
import org.readium.r2.streamer.parser.EpubParser
import org.jetbrains.anko.*

class BookListAdapter(private var bookListener: OnBookClickListener) : RecyclerView.Adapter<BookListAdapter.BookListViewHolder>() {

    private var listOfBooks = mutableListOf<BookInfo>()
    private var book1: Int = 0

    companion object {
        private const val TAG = "BookListAdapter"
        private const val REQUEST_CODE = 100
    }

    inner class BookListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById<View>(R.id.title) as TextView
        val textViewAuthor: TextView = itemView.findViewById<View>(R.id.author) as TextView
        val thumbNail: ImageView = itemView.findViewById(R.id.thumbnail) as ImageView
        val readBtn: Button = itemView.findViewById(R.id.readBtn)
        val infoHolder: RelativeLayout = itemView.findViewById(R.id.infoGroup)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListViewHolder {
        return BookListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.book_item,
                parent,
                false
            )
        )
    }

    /**
     * Method responsible for loading data when user scroll.
     */
    override fun onBindViewHolder(holder: BookListViewHolder, position: Int) {
        val book = listOfBooks[position]
        // Bind items
        holder.textViewTitle.text = book.title
        holder.textViewTitle.contentDescription = "\u00A0"

        if (book.title!=null) {
            holder.thumbNail.contentDescription = book.title
        }

        if(book.author!=null){
            holder.textViewAuthor.text = book.author
        }

        book.cover?.let {
            val arrayInputStream = ByteArrayInputStream(it)
            val bitmap = BitmapFactory.decodeStream(arrayInputStream)
            holder.thumbNail.setImageBitmap(bitmap)
        } ?: run {
            Timber.e("Image was not found.")
            holder.thumbNail.setImageResource(R.drawable.allatra_ru)
//            book.coverLink?.let {
//                val baseUrl = server + "/" + book.fileName + it
//                Picasso.with(activity).load(baseUrl).into(viewHolder.imageView)
//            }
        }

        holder.readBtn.setOnClickListener(View.OnClickListener { view ->
            val dialog = getProgressDialog(view.context)

            task {
                val bookToOpen = listOfBooks[position]
                bookListener.bookListClicked(bookToOpen)
                Timber.tag(TAG).d( "Book to open: $book1")
                bookToOpen.fileAbsolutePath?.let {
                    val context = holder.itemView.context
                    val intent = Intent(context, R2ReaderActivity::class.java)
                    intent.putExtra("publicationPath", bookToOpen.fileAbsolutePath)
                    intent.putExtra("epubName", bookToOpen.fileName)
                    //TODO> for some reason cannot send this - too big i think
                    //intent.putExtra("publication", pub!!.publication)
                    intent.putExtra("bookId", bookToOpen.id)

                    startActivityForResult(context as MainActivity, intent, REQUEST_CODE, intent.extras)
                }
            } then {
                dialog.hide()
            } fail {
                dialog.hide()
            }
        })

        // This attaches listener to see where user clicked
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                //val context = holder.itemView.context
                holder.infoHolder.visibility = if (holder.infoHolder.visibility.equals(View.GONE)) View.VISIBLE else {
                    notifyDataSetChanged()
                    View.GONE
                }
            }
        })
    }

    interface OnBookClickListener {
        //this is method to handle the event when clicked on the image in Recyclerview
        fun bookListClicked(bookInfo: BookInfo)
    }

    fun setList(listOfBooks: MutableList<BookInfo>) {
        this.listOfBooks = listOfBooks
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listOfBooks.size

    private fun getProgressDialog(context: Context): AlertDialog {
        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(context)
        tvText.text = "Loading ..."
        tvText.setTextColor(Color.GRAY)
        tvText.textSize = 20f
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(ll)

        val dialog = builder.create()
        dialog.show()
        val window = dialog.window

        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(window.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            window.attributes = layoutParams
        }

        return dialog
    }
}