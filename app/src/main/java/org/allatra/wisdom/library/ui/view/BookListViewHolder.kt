package org.allatra.wisdom.library.ui.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.book_item.view.*
import com.bumptech.glide.Glide
import org.allatra.wisdom.library.db.BookInfo

class BookListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(book: BookInfo) {
        itemView.textBookName.text = book.getTitle()
        itemView.textBookDescription.text = book.getDescription()
        itemView.textReadPages.text = "Read: "+book.getIndexPage().toString() + "/" + book.getTotalPages()

        Glide.with(itemView.context).load(book.getThumbNail()).into(itemView.thumbNail)
    }
}
