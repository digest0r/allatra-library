package org.allatra.wisdom.library.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.allatra.wisdom.library.ui.adapter.BookListAdapter
import org.allatra.wisdom.library.db.DatabaseHandler
import org.allatra.wisdom.library.static.EnumDefinition
import android.content.res.Configuration
import org.allatra.wisdom.library.db.BookInfo
import org.allatra.wisdom.library.lang.LocaleManager
import java.util.*
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import android.widget.ImageView
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import org.allatra.wisdom.library.R
import org.readium.r2.shared.Injectable
import org.readium.r2.streamer.parser.EpubParser
import org.readium.r2.streamer.server.Server
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.ServerSocket
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.allatra.wisdom.library.BuildConfig
import org.allatra.wisdom.library.LocaleManager
import org.allatra.wisdom.library.ui.decoration.GridSpacingItemDecoration
import org.readium.r2.streamer.parser.PubBox
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), BookListAdapter.OnBookClickListener, CoroutineScope {
    /**
     * Context of this scope.
     */
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


    private var databaseHandler: DatabaseHandler? = null
    private val localeManager = LocaleManager()
    var alertLanguages: AlertDialog? = null
    private lateinit var adapter: BookListAdapter
    // Book list must be defined there as this instance operates with it
    private lateinit var listOfBooks: MutableList<BookInfo>
    private var localLang: String = "English"
    private var localLanguage: EnumDefinition.EnLanguage? = null
    private lateinit var workingDir: String
    private lateinit var preferences: SharedPreferences

    private lateinit var server: Server
    private var localPort: Int = 0
    private var useExternalFileDir = false

    companion object {
        private const val TAG = "MainActivity"
        private const val WRONG_PDF_ID = 111111
        private const val ASSETS_BOOKS_ROOT = "books"
        private const val SHARED_PREFERENCES_NAME = "org.allatra.wisdom"
        private const val EPUB_POSTFIX = ".epub"
        private const val SERVER_SOCKET_PORT = 0
        private const val USE_EXTERNAL_DIR = "useExternalFileDir"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var localeManager = LocaleManager()
        var lang = localeManager.loadSharedResources(this, true)
        setContentView(R.layout.activity_main)
        textView.text = lang
        setTitle(resources.getString(R.string.app_name));
//        localeManager.setLanguage() default
//        setSupportActionBar(toolbar)
//        supportActionBar!!.setDisplayShowCustomEnabled(true)
//        supportActionBar!!.setLogo(R.mipmap.lotus_flower)
//        supportActionBar!!.setDisplayUseLogoEnabled(true)
        val toolbar = toolbar
        setSupportActionBar(toolbar)

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        databaseHandler = DatabaseHandler(this)
        initServer()
        initData()
        initView()
    }

    /**
     * On activityResult we have to update list in the adapter as number of actual page might have changed.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        Timber.tag(TAG).e("Activity result.")
        if (resultCode == RESULT_OK && intent != null) {
            val pdfId = intent.getLongExtra("pdfId", WRONG_PDF_ID.toLong())

            if(pdfId != WRONG_PDF_ID.toLong()){
                adapter.setList(getFilteredBooks(localLanguage!!))
            }
        } else {
            Timber.tag(TAG).e("ResultCode=$resultCode, requestCode=$requestCode, $intent")
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

    override fun onStart() {
        super.onStart()
        startServer()
        //readBooksFromAssets(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopServer()
    }

    private fun readBooksFromAssets(){
        var isNewPublication = false
        EnumDefinition.EnLanguage.values().forEach { enLanguage ->
            Timber.tag(TAG).d( "Checking books under: $enLanguage.")

            assets.list("$ASSETS_BOOKS_ROOT/${enLanguage.abbreviation}")?.filter { it.endsWith(EPUB_POSTFIX) }?.let { list ->
                val listOfRegisteredBookInfo = databaseHandler!!.getListOfBookInfo()
                list.forEach { bookFileName ->
                    Timber.tag(TAG).d( "Processing publication: $bookFileName")
                    // Get UUID based on the book
                    val fileUUID = UUID.nameUUIDFromBytes(bookFileName.toByteArray())

                    // Check if book exists
                    var bookInfo = listOfRegisteredBookInfo.filter {
                        it.fileUUID!!.compareTo(fileUUID.toString()) == 0
                    }.toList().firstOrNull()

                    if (bookInfo == null) {
                        isNewPublication = true
                        val publicationPath = workingDir + fileUUID
                        val pubBox = getPubBox(bookFileName, publicationPath, enLanguage)

                        if (pubBox != null) {
                            // Creates a db object of books
                            databaseHandler!!.createBookInfoObject(
                                pubBox,
                                bookFileName,
                                fileUUID.toString(),
                                publicationPath,
                                enLanguage
                            )
                        } else {
                            Timber.tag(TAG).e("Book: pupBox is null.")
                        }
                    } else {
                        Timber.tag(TAG).d("Book: $bookFileName already exists in database.")
                    }
                }
            }
        }

        if (isNewPublication){
            databaseHandler?.let { it.commitRecords() }
            Timber.tag(TAG).i( "New publications were added.")
        } else {
            Timber.tag(TAG).i( "No new publications added.")
        }
    }


    override fun bookListClicked(bookInfo: BookInfo) {
        addPublicationToServer(bookInfo.fileUUID!!, bookInfo.fileAbsolutePath!!, bookInfo.fileName!!, bookInfo.getLanguageEnum())
    }

    private fun getPubBox(bookFileName: String, publicationPath: String, enLanguage: EnumDefinition.EnLanguage): PubBox?{
        val workingFile = File(publicationPath)

        if(!workingFile.exists()){
            Timber.tag(TAG).d("Book: $bookFileName will be written into: $publicationPath.")
            // Get inputStream
            val inputStream = assets.open("$ASSETS_BOOKS_ROOT/${enLanguage.abbreviation}/$bookFileName")
            // Write file out
            inputStream.writeToFile(publicationPath)
            // Creates the file
            File(publicationPath)
        }

        val epubParser = EpubParser()
        return epubParser.parse(publicationPath)
    }

    private fun addPublicationToServer(fileUUID: String, publicationPath: String, bookFileName: String, enLanguage: EnumDefinition.EnLanguage){

        launch {
            val pubBox = getPubBox(bookFileName, publicationPath, enLanguage)

            pubBox?.let {
                val publicationIdentifier = pubBox.publication.metadata.identifier
                // Preferences and publication port
                preferences.edit().putString("$publicationIdentifier-publicationPort", localPort.toString())
                    .apply()

                if(server.isAlive) {
                    // Add pub to the server
                    server.addEpub(
                        pubBox.publication,
                        pubBox.container,
                        "/$fileUUID",
                        applicationContext.filesDir.path + "/" + Injectable.Style.rawValue + "/UserProperties.json"
                    )
                    Timber.tag(TAG).d("Book: $bookFileName added to the server.")
                } else {
                    Timber.tag(TAG).e("Server is not alive, cannot add a book.")
                }
            } ?: run {
                Timber.tag(TAG).e("Book: pupBox is null.")
            }
        }
    }

    private fun InputStream.writeToFile(path: String) {
        use { input ->
            File(path).outputStream().use { input.copyTo(it) }
        }
    }

    private fun showDialogWithLanguages(){
        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("Select your language: ")
        var mContext: Context
        var res: Resources

        builder.setSingleChoiceItems(getLanguagesLocal(), -1) { dialog, item ->
            when (item) {
                0 -> {
                    localLang = getString(R.string.text_language_en)
                    localLanguage = EnumDefinition.EnLanguage.EN
                    Log.d(TAG, "Language is changed to: $item, EN")
//                    adapter.setList(getFilteredBooks(localLanguage!!))
                    mContext = localeManager.changeLanguage(this, "en")
                    res = mContext.resources
                    textView.text = res.getString(R.string.app_name)
                    recreate()
                    Timber.tag(TAG).d( "Language is changed to: $item, EN")
                    adapter.setList(getFilteredBooks(localLanguage!!))
                }

                1 -> {
                    localLang = getString(R.string.text_language_ru)
                    localLanguage = EnumDefinition.EnLanguage.RU
                    Timber.tag(TAG).d( "Language is changed to: $item, RU")
                    adapter.setList(getFilteredBooks(localLanguage!!))
//                    localLang = getString(R.string.text_language_ru)
//                    localLanguage = EnumDefinition.EnLanguage.RU
                    Log.d(TAG, "Language is changed to: $item, RU")
//                    adapter.setList(getFilteredBooks(localLanguage!!))
                    mContext = localeManager.changeLanguage(this, "ru")
                    res = mContext.resources
                    textView.text = res.getString(R.string.app_name)
                    recreate()
                }

                2 ->{
                    localLang = getString(R.string.text_language_ua)
                    localLanguage = EnumDefinition.EnLanguage.UA
                    Timber.tag(TAG).d( "Language is changed to: $item, UA")
                    adapter.setList(getFilteredBooks(localLanguage!!))
//                    localLang = getString(R.string.text_language_ua)
//                    localLanguage = EnumDefinition.EnLanguage.UA
                    Log.d(TAG, "Language is changed to: $item, UA")
//                    adapter.setList(getFilteredBooks(localLanguage!!))
                    mContext = localeManager.changeLanguage(this, "uk")
                    res = mContext.resources
                    textView.text = res.getString(R.string.app_name)
                    recreate();
                }

                3 ->{
                    localLang = getString(R.string.text_language_cz)
                    localLanguage = EnumDefinition.EnLanguage.CS
                    Timber.tag(TAG).d( "Language is changed to: $item, CS")
                    adapter.setList(getFilteredBooks(localLanguage!!))
                    Log.d(TAG, "Language is changed to: $item, CS")
//                    adapter.setList(getFilteredBooks(localLanguage!!))
                    mContext = localeManager.changeLanguage(this, "cs")
                    res = mContext.resources
                    textView.text = res.getString(R.string.app_name)
                    recreate()
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

    private fun startServer() {
        if (!server.isAlive) {
            try {
                server.start()
                Timber.tag(TAG).d("READIUM Server has been started.")
            } catch (e: IOException) {
                // do nothing
                Timber.e(e)
            }
            if (server.isAlive) {
                Timber.tag(TAG).d("READIUM Server is alive.")
                // Add Resources from R2Navigator
                server.loadReadiumCSSResources(assets)
                server.loadR2ScriptResources(assets)
                server.loadR2FontResources(assets, applicationContext)
            }
        }
    }

    private fun stopServer() {
        if (server.isAlive) {
            server.stop()
        }
    }

    private fun initView(){
        // Init collapsing toolbar
        initCollapsingToolbar()
        // back drop lotus image
        val backDrop = findViewById<ImageView>(R.id.backdrop)
        backDrop.setImageResource(R.drawable.big_lotus)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewBooks)
        val mLayoutManager = GridLayoutManager(this, 2)

        recyclerView.layoutManager = mLayoutManager
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, 10, true, this))
        recyclerView.itemAnimator = DefaultItemAnimator() as RecyclerView.ItemAnimator?
        recyclerView.adapter = adapter
    }

    /**
     * Takes care of initializing backend information.
     */
    private fun initData() {
        // Init share preferences
        preferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        // Init working dir
        initWorkingPath()
        // Read all books from assets folder, do not add to the server yet
        readBooksFromAssets()
        // System language
        val systemLangList = ConfigurationCompat.getLocales(Resources.getSystem().configuration)
        // Init user settings
        initUserLanguage(systemLangList)
        // Get all the books
        listOfBooks = databaseHandler?.getListOfBookInfo()!!
        // Init adapter
        adapter = BookListAdapter(this)
        // Do the filter
        adapter.setList(getFilteredBooks(localLanguage!!))
        // Start the server
    }

    private fun initWorkingPath(){
        if(useExternalFileDir){
            workingDir = this.getExternalFilesDir(null)?.path + "/"
        } else {
            workingDir = this.filesDir.path + "/"
        }
        Timber.tag(TAG).d("Working dir set to: $workingDir")
    }

    private fun initUserLanguage(localeListCompat: LocaleListCompat){
        localLanguage = databaseHandler!!.initUserSettingsAndReturn(localeListCompat)
    }

    private fun initServer(){
        val s = ServerSocket(SERVER_SOCKET_PORT)
        s.localPort
        s.close()

        localPort = s.localPort
        server = Server(localPort)
    }

    fun getFilteredBooks(language: EnumDefinition.EnLanguage): MutableList<BookInfo> {
        return listOfBooks.filter { bookInfo -> bookInfo.getLanguageEnum().equals(language) }.sortedBy { bookInfo -> bookInfo.id }.toMutableList()
    }

     /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private fun initCollapsingToolbar() {
        val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbar)

        collapsingToolbar.title = " "
        val appBarLayout = findViewById<AppBarLayout>(R.id.appbar)
        appBarLayout.setExpanded(true)

         appBarLayout.addOnOffsetChangedListener(
             object: AppBarLayout.OnOffsetChangedListener{
                 override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                     val scrollRange = appBarLayout!!.totalScrollRange

                     if(verticalOffset + scrollRange < 30 ) {
                         collapsingToolbar.title = getString(R.string.app_name)
                     } else {
                         collapsingToolbar.title = " "
                     }
                 }
             }
         )
    }
}
