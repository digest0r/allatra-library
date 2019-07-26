package org.allatra.wisdom.library

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.allatra.wisdom.library.adapter.BookListAdapter
import org.allatra.wisdom.library.decoration.DividerItemDecoration
import org.allatra.wisdom.library.decoration.VerticalSpaceItemDecoration
import org.allatra.wisdom.library.model.Book

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setLogo(R.mipmap.lotus_flower)
        supportActionBar!!.setDisplayUseLogoEnabled(true)

        initView()
    }

    private fun initView(){
        recyclerViewBooks.layoutManager = LinearLayoutManager(this)
        recyclerViewBooks.addItemDecoration(VerticalSpaceItemDecoration(48))

        //This will for default android divider
        recyclerViewBooks.addItemDecoration(DividerItemDecoration(this))
        val adapter = BookListAdapter()
        recyclerViewBooks.adapter = adapter
        adapter.setList(geData())
    }

    private fun geData(): MutableList<Book>{
        val allBooks = mutableListOf<Book>()

        var book = Book("Sensei of Shambala. Book I", "",
            "In this Book, you will find the answers to the most important questions in life...", 1, 722, R.drawable.sensei1_en)
        allBooks.add(book)

        book = Book("Sensei of Shambala. Book II", "",
            "You can hide neither from the Truth nor from Wisdom. There is nothing secret on Earth which will not one day become known. ", 1, 722, R.drawable.sensei2_en)
        allBooks.add(book)
        return allBooks
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
