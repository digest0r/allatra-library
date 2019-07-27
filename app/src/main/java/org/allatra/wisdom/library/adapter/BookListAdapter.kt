package org.allatra.wisdom.library.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.allatra.wisdom.library.R
import org.allatra.wisdom.library.db.BookInfo
import org.allatra.wisdom.library.model.Book
import org.allatra.wisdom.library.view.BookListViewHolder

class BookListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfBooks = mutableListOf<BookInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BookListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false))
    }

    override fun getItemCount(): Int = listOfBooks.size

    /**
     * Method responsible for loading data when user scroll.
     */
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val movieViewHolder = viewHolder as BookListViewHolder
        movieViewHolder.bindView(listOfBooks[position])
    }

    fun setList(listOfBooks: MutableList<BookInfo>) {
        this.listOfBooks = listOfBooks
        notifyDataSetChanged()
    }
}