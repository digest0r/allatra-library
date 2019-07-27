package org.allatra.wisdom.library.db

import android.content.Context
import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration
import org.allatra.wisdom.library.R
import org.allatra.wisdom.library.static.EnumDefinition

class DatabaseHandler(private val context: Context) {

    private lateinit var realm: Realm
    private lateinit var listOfBooks: MutableList<BookInfo>

    companion object {
        private const val TAG = "DB"
    }

    private fun initRealm(){
        // Initialize Realm
        Realm.init(context)
        val config = RealmConfiguration.Builder()
            .name(context.getString(R.string.app_realm_name))
            .schemaVersion(3)
            .deleteRealmIfMigrationNeeded()
            .build()
        realm = Realm.getInstance(config)
    }

    fun initDb(){
        initRealm()
        // Try to fetch existing records
        listOfBooks = realm.where(BookInfo::class.java).findAll()

        if(listOfBooks.size==0){
            Log.i(TAG, "Db was initialized with new records.")
            realm.beginTransaction()
            createBooksEN()
            realm.commitTransaction()

            listOfBooks = realm.where(BookInfo::class.java).findAll()
        } else {
            Log.i(TAG, "Db has records. Size: ${listOfBooks.size}")
        }
    }

    fun deleteAll(){
        //Realm.deleteRealm(config)
        // Get a Realm instance for this thread
        //realm = Realm.getDefaultInstance()
        initRealm()

        realm?.let {
            it.deleteAll()
        }
    }

    /**
     * Fills all the books available in english language.
     */
    private fun createBooksEN(){
        var book = BookInfo()
        // sensei 1
        book.setId(1)
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(context.getString(R.string.sensei_1_title_en))
        book.setDescription(context.getString(R.string.sensei_1_description_en))
        book.setIndexPage(0)
        book.setTotalPages(context.getString(R.string.sensei_1_total_pages_en).toInt())
        book.setThumbNail(R.drawable.sensei_1_en)
        book.setPdfName(context.getString(R.string.sensei_1_book_pdf_en))
        realm.copyToRealm(book)

        // sensei 2
        book = BookInfo()
        book.setId(2)
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(context.getString(R.string.sensei_2_title_en))
        book.setDescription(context.getString(R.string.sensei_2_description_en))
        book.setIndexPage(0)
        book.setTotalPages(context.getString(R.string.sensei_2_total_pages_en).toInt())
        book.setThumbNail(R.drawable.sensei_2_en)
        book.setPdfName(context.getString(R.string.sensei_2_book_pdf_en))
        realm.copyToRealm(book)

        // sensei 3
        book = BookInfo()
        book.setId(3)
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(context.getString(R.string.sensei_3_title_en))
        book.setDescription(context.getString(R.string.sensei_3_description_en))
        book.setIndexPage(0)
        book.setTotalPages(context.getString(R.string.sensei_3_total_pages_en).toInt())
        book.setThumbNail(R.drawable.sensei_3_en)
        book.setPdfName(context.getString(R.string.sensei_3_book_pdf_en))
        realm.copyToRealm(book)

        // sensei 4
        book = BookInfo()
        book.setId(4)
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(context.getString(R.string.sensei_4_title_en))
        book.setDescription(context.getString(R.string.sensei_4_description_en))
        book.setIndexPage(0)
        book.setTotalPages(context.getString(R.string.sensei_4_total_pages_en).toInt())
        book.setThumbNail(R.drawable.sensei_4_en)
        book.setPdfName(context.getString(R.string.sensei_4_book_pdf_en))
        realm.copyToRealm(book)

        // allatra
        book = BookInfo()
        book.setId(5)
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(context.getString(R.string.allatra_title_en))
        book.setDescription(context.getString(R.string.allatra_description_en))
        book.setIndexPage(0)
        book.setTotalPages(context.getString(R.string.allatra_total_pages_en).toInt())
        book.setThumbNail(R.drawable.allatra_en)
        book.setPdfName(context.getString(R.string.allatra_book_pdf_en))
        realm.copyToRealm(book)

        // Consciousness and Personality
        book = BookInfo()
        book.setId(6)
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(context.getString(R.string.csc_title_en))
        book.setDescription(context.getString(R.string.csc_description_en))
        book.setIndexPage(0)
        book.setTotalPages(context.getString(R.string.csc_total_pages_en).toInt())
        book.setThumbNail(R.drawable.csc_and_personality_en)
        book.setPdfName(context.getString(R.string.csc_book_pdf_en))
        realm.copyToRealm(book)

        // Birds and stone
        book = BookInfo()
        book.setId(7)
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(context.getString(R.string.birds_and_stone_title_en))
        book.setDescription(context.getString(R.string.birds_and_stone_description_en))
        book.setIndexPage(0)
        book.setTotalPages(context.getString(R.string.birds_and_stone_total_pages_en).toInt())
        book.setThumbNail(R.drawable.birds_and_stone_en)
        book.setPdfName(context.getString(R.string.birds_and_stone_book_pdf_en))
        realm.copyToRealm(book)

        // Ezoosmos
        book = BookInfo()
        book.setId(8)
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(context.getString(R.string.ezoosmos_title_en))
        book.setDescription(context.getString(R.string.ezoosmos_description_en))
        book.setIndexPage(0)
        book.setTotalPages(context.getString(R.string.ezoosmos_total_pages_en).toInt())
        book.setThumbNail(R.drawable.ezoosmos_en)
        book.setPdfName(context.getString(R.string.ezoosmos_book_pdf_en))
        realm.copyToRealm(book)

        // Global climate: report
        book = BookInfo()
        book.setId(9)
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(context.getString(R.string.global_climate_title_en))
        book.setDescription(context.getString(R.string.global_climate_description_en))
        book.setIndexPage(0)
        book.setTotalPages(context.getString(R.string.global_climate_total_pages_en).toInt())
        book.setThumbNail(R.drawable.global_climate_en)
        book.setPdfName(context.getString(R.string.global_climate_book_pdf_en))
        realm.copyToRealm(book)

        // Practices and meditations
        book = BookInfo()
        book.setId(10)
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(context.getString(R.string.practices_and_meditations_title_en))
        book.setDescription(context.getString(R.string.practices_and_meditations_description_en))
        book.setIndexPage(0)
        book.setTotalPages(context.getString(R.string.practices_and_meditations_total_pages_en).toInt())
        book.setThumbNail(R.drawable.practices_and_meditations_en)
        book.setPdfName(context.getString(R.string.practices_and_meditations_book_pdf_en))
        realm.copyToRealm(book)

        // Predictions and future
        book = BookInfo()
        book.setId(11)
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(context.getString(R.string.predictions_of_the_future_title_en))
        book.setDescription(context.getString(R.string.predictions_of_the_future_description_en))
        book.setIndexPage(0)
        book.setTotalPages(context.getString(R.string.predictions_of_the_future_total_pages_en).toInt())
        book.setThumbNail(R.drawable.predictions_of_the_future_en)
        book.setPdfName(context.getString(R.string.predictions_of_the_future_book_pdf_en))
        realm.copyToRealm(book)

        // Primordial allatra physics
        book = BookInfo()
        book.setId(12)
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(context.getString(R.string.primordial_physics_title_en))
        book.setDescription(context.getString(R.string.primordial_physics_description_en))
        book.setIndexPage(0)
        book.setTotalPages(context.getString(R.string.primordial_physics_total_pages_en).toInt())
        book.setThumbNail(R.drawable.primordial_physics_en)
        book.setPdfName(context.getString(R.string.primordial_physics_book_pdf_en))
        realm.copyToRealm(book)
    }

    fun getFilteredBooks(language: EnumDefinition.EnLanguage): MutableList<BookInfo> {
        return listOfBooks.filter { bookInfo -> bookInfo.getLanguageEnum().equals(language) }.toMutableList()
    }
}