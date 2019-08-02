package org.allatra.wisdom.library

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.allatra.wisdom.library.ui.adapter.BookListAdapter
import org.allatra.wisdom.library.db.DatabaseHandler
import org.allatra.wisdom.library.ui.decoration.DividerItemDecoration
import org.allatra.wisdom.library.ui.decoration.VerticalSpaceItemDecoration
import org.allatra.wisdom.library.static.EnumDefinition
import android.content.res.Configuration
import org.allatra.wisdom.library.db.BookInfo
import org.allatra.wisdom.library.lang.LocaleManager
import java.util.*
import android.content.Intent
import android.content.res.Resources
import androidx.core.os.ConfigurationCompat


class MainActivity : AppCompatActivity() {
    private var databaseHandler: DatabaseHandler? = null
    private val localeManager = LocaleManager()
    var alertLanguages: AlertDialog? = null
    private lateinit var adapter: BookListAdapter
    // Book list must be defined there as this instance operates with it
    private lateinit var listOfBooks: MutableList<BookInfo>
    private var localLang: String = "English"
    private var localLanguage: EnumDefinition.EnLanguage? = null

    companion object {
        private const val TAG = "MainActivity"
        private const val WRONG_PDF_ID = 111111
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

    /**
     * On activityResult we have to update list in the adapter as number of actual page might have changed.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        Log.d(TAG, "Activity result.")
        if (resultCode == RESULT_OK && intent != null) {
            val pdfId = intent.getLongExtra("pdfId", WRONG_PDF_ID.toLong())

            if(pdfId != WRONG_PDF_ID.toLong()){
                adapter.setList(getFilteredBooks(localLanguage!!))
            }
        } else {
            Log.e(TAG, "ResultCode=$resultCode, requestCode=$requestCode, $intent")
        }
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(localeManager.setLocale(context, localLang))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeManager.setLocale(this, localLang)
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

    private fun showDialogWithLanguages(){
        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("Select your language: ")

        builder.setSingleChoiceItems(getLanguagesLocal(), -1) { dialog, item ->
            when (item) {
                0 -> {
                    localLang = getString(R.string.text_language_en)
                    localLanguage = EnumDefinition.EnLanguage.EN
                    Log.d(TAG, "Language is changed to: $item, EN")
                    adapter.setList(getFilteredBooks(localLanguage!!))
                    localeManager.setLocale(this, localLang)
                }

                1 -> {
                    localLang = getString(R.string.text_language_ru)
                    localLanguage = EnumDefinition.EnLanguage.RU
                    Log.d(TAG, "Language is changed to: $item, RU")
                    adapter.setList(getFilteredBooks(localLanguage!!))
                    localeManager.setLocale(this, localLang)
                }

                2 ->{
                    localLang = getString(R.string.text_language_ua)
                    localLanguage = EnumDefinition.EnLanguage.UA
                    Log.d(TAG, "Language is changed to: $item, UA")
                    adapter.setList(getFilteredBooks(localLanguage!!))
                    localeManager.setLocale(this, localLang)
                }

                3 ->{
                    localLang = getString(R.string.text_language_cz)
                    localLanguage = EnumDefinition.EnLanguage.CZ
                    Log.d(TAG, "Language is changed to: $item, CZ")
                    adapter.setList(getFilteredBooks(localLanguage!!))
                    localeManager.setLocale(this, localLang)
                    attachBaseContext(this)
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
        localLanguage = EnumDefinition.EnLanguage.EN
        adapter.setList(getFilteredBooks(EnumDefinition.EnLanguage.EN))
    }

    private fun initData() {
        databaseHandler?.let {
            // Language of app
            val lang = Locale.getDefault().displayLanguage
            // System language
            val systemLangList = ConfigurationCompat.getLocales(Resources.getSystem().configuration)
            // Actual lang
            val actualLang = systemLangList[0]

            listOfBooks = it.getListOfBookInfo(Locale.getDefault().displayLanguage)
        } ?: run {
            Log.e("Error", "DatabaseHandler was not initialized.")
        }
    }

    fun getFilteredBooks(language: EnumDefinition.EnLanguage): MutableList<BookInfo> {
        return listOfBooks.filter { bookInfo -> bookInfo.getLanguageEnum().equals(language) }.sortedBy { bookInfo -> bookInfo.getId() }.toMutableList()
    }
}
