package org.allatra.wisdom.library

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.allatra.wisdom.library.adapter.BookListAdapter
import org.allatra.wisdom.library.db.DatabaseHandler
import org.allatra.wisdom.library.decoration.DividerItemDecoration
import org.allatra.wisdom.library.decoration.VerticalSpaceItemDecoration
import org.allatra.wisdom.library.model.Book
import org.allatra.wisdom.library.static.EnumDefinition
import android.widget.Toast
import android.content.DialogInterface
import java.util.*


class MainActivity : AppCompatActivity() {

    private var databaseHandler: DatabaseHandler? = null
    var alertLanguages: AlertDialog? = null
    private lateinit var adapter: BookListAdapter

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setLogo(R.mipmap.lotus_flower)
        supportActionBar!!.setDisplayUseLogoEnabled(true)

        databaseHandler = DatabaseHandler(this)
        initData()
        initView()
    }

    private fun showDialogWithLanguages(){
        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("Select your language: ")

        builder.setSingleChoiceItems(getLanguagesLocal(), -1) { dialog, item ->
            when (item) {
                0 -> {
                    Log.d(TAG, "Language is changed to: $item, EN")
                    adapter.setList(databaseHandler!!.getFilteredBooks(EnumDefinition.EnLanguage.EN))
                }

                1 -> {
                    Log.d(TAG, "Language is changed to: $item, RU")
                    adapter.setList(databaseHandler!!.getFilteredBooks(EnumDefinition.EnLanguage.RU))
                }

                2 ->{
                    Log.d(TAG, "Language is changed to: $item, UA")
                    adapter.setList(databaseHandler!!.getFilteredBooks(EnumDefinition.EnLanguage.UA))
                }
            }
            alertLanguages!!.dismiss()
        }
        alertLanguages = builder.create()
        alertLanguages!!.show()
    }

    private fun getLanguagesLocal(): Array<CharSequence> {
        val languagesTextArray = arrayOf<CharSequence>(
            getString(R.string.text_language_en),
            getString(R.string.text_language_ru),
            getString(R.string.text_language_ua),
            getString(R.string.text_language_cz),
            getString(R.string.text_language_bg),
            getString(R.string.text_language_de),
            getString(R.string.text_language_pl),
            getString(R.string.text_language_lt),
            getString(R.string.text_language_it))

        return languagesTextArray
    }

    private fun initView(){
        recyclerViewBooks.layoutManager = LinearLayoutManager(this)
        recyclerViewBooks.addItemDecoration(VerticalSpaceItemDecoration(48))

        //This will for default android divider
        recyclerViewBooks.addItemDecoration(DividerItemDecoration(this))

        // Init adapter
        adapter = BookListAdapter()
        recyclerViewBooks.adapter = adapter
        adapter.setList(databaseHandler!!.getFilteredBooks(EnumDefinition.EnLanguage.EN))
    }

    private fun initData(){
        databaseHandler?.let {
            it.initDb()
            //TODO: Check the main language of user - store into DB
            val string = Locale.getDefault().displayLanguage
            Log.i(TAG, "Language is $string")

            Log.i(TAG, "Get records.")
            val records = it.getFilteredBooks(EnumDefinition.EnLanguage.EN)
            Log.d(TAG, "Get records. Records = $records")
        }?: run {
            Log.e("Error", "DatabaseHandler was not initialized.")
        }
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

            R.id.action_language -> {
                showDialogWithLanguages()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
