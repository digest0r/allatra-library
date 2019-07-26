package org.allatra.wisdom.library.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.book_item.view.*
import org.allatra.wisdom.library.model.Book
import com.bumptech.glide.Glide

class BookListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(book: Book) {
        itemView.textBookName.text = book.name
        itemView.textBookDescription.text = book.description
        itemView.textReadPages.text = book.actualPage.toString() + "/" + book.totalPages

        Glide.with(itemView.context).load(book.thumbNail).into(itemView.thumbNail)
    }
}
