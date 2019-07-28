package org.allatra.wisdom.library.db

import android.content.Context
import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration
import org.allatra.wisdom.library.R
import org.allatra.wisdom.library.static.EnumDefinition
import org.allatra.wisdom.library.static.StaticDefinition

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
            .name(StaticDefinition.app_realm_name)
            .schemaVersion(3)
            .deleteRealmIfMigrationNeeded()
            .build()
        realm = Realm.getInstance(config)
    }

    fun initDb(language: String){
        Log.i(TAG, "Language is $language")
        initRealm()
        //deleteAllRecords()
        // Init user settings
        initUserSettings(language)
        // Try to fetch existing records
        listOfBooks = realm.where(BookInfo::class.java).findAll()

        if(listOfBooks.size==0){
            Log.i(TAG, "Db was initialized with new records.")
            realm.beginTransaction()
            createBooksEN()
            createBooksRU()
            createBooksUA()
            realm.commitTransaction()

            listOfBooks = realm.where(BookInfo::class.java).findAll()
        } else {
            Log.i(TAG, "Db has records. Size: ${listOfBooks.size}")
        }
    }

    fun deleteAllRecords(){
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()
    }

    fun initUserSettings(language: String){
        var userSettings = UserSettings()
        userSettings.setId(0)
        userSettings.setLanguage(language)
    }

    /**
     * Fills all the books available in english language.
     */
    private fun createBooksEN(){
        var book = BookInfo()
        // sensei 1
        book.setId(StaticDefinition.sensei_1_id_en.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(StaticDefinition.sensei_1_title_en)
        book.setDescription(StaticDefinition.sensei_1_description_en)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.sensei_1_total_pages_en)
        book.setThumbNail(R.drawable.sensei_1_en)
        book.setPdfName(StaticDefinition.sensei_1_book_pdf_en)
        realm.copyToRealm(book)

        // sensei 2
        book = BookInfo()
        book.setId(StaticDefinition.sensei_2_id_en.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(StaticDefinition.sensei_2_title_en)
        book.setDescription(StaticDefinition.sensei_2_description_en)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.sensei_2_total_pages_en)
        book.setThumbNail(R.drawable.sensei_2_en)
        book.setPdfName(StaticDefinition.sensei_2_book_pdf_en)
        realm.copyToRealm(book)

        // sensei 3
        book = BookInfo()
        book.setId(StaticDefinition.sensei_3_id_en.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(StaticDefinition.sensei_3_title_en)
        book.setDescription(StaticDefinition.sensei_3_description_en)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.sensei_3_total_pages_en)
        book.setThumbNail(R.drawable.sensei_3_en)
        book.setPdfName(StaticDefinition.sensei_3_book_pdf_en)
        realm.copyToRealm(book)

        // sensei 4
        book = BookInfo()
        book.setId(StaticDefinition.sensei_4_id_en.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(StaticDefinition.sensei_4_title_en)
        book.setDescription(StaticDefinition.sensei_4_description_en)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.sensei_4_total_pages_en)
        book.setThumbNail(R.drawable.sensei_4_en)
        book.setPdfName(StaticDefinition.sensei_4_book_pdf_en)
        realm.copyToRealm(book)

        // allatra
        book = BookInfo()
        book.setId(StaticDefinition.allatra_id_en.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(StaticDefinition.allatra_title_en)
        book.setDescription(StaticDefinition.allatra_description_en)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.allatra_total_pages_en)
        book.setThumbNail(R.drawable.allatra_en)
        book.setPdfName(StaticDefinition.allatra_book_pdf_en)
        realm.copyToRealm(book)

        // Consciousness and Personality
        book = BookInfo()
        book.setId(StaticDefinition.csc_id_en.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(StaticDefinition.csc_title_en)
        book.setDescription(StaticDefinition.csc_description_en)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.csc_total_pages_en)
        book.setThumbNail(R.drawable.csc_and_personality_en)
        book.setPdfName(StaticDefinition.csc_book_pdf_en)
        realm.copyToRealm(book)

        // Birds and stone
        book = BookInfo()
        book.setId(StaticDefinition.birds_and_stone_id_en.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(StaticDefinition.birds_and_stone_title_en)
        book.setDescription(StaticDefinition.birds_and_stone_description_en)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.birds_and_stone_total_pages_en)
        book.setThumbNail(R.drawable.birds_and_stone_en)
        book.setPdfName(StaticDefinition.birds_and_stone_book_pdf_en)
        realm.copyToRealm(book)

        // Ezoosmos
        book = BookInfo()
        book.setId(StaticDefinition.ezoosmos_id_en.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(StaticDefinition.ezoosmos_title_en)
        book.setDescription(StaticDefinition.ezoosmos_description_en)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.ezoosmos_total_pages_en)
        book.setThumbNail(R.drawable.ezoosmos_en)
        book.setPdfName(StaticDefinition.ezoosmos_book_pdf_en)
        realm.copyToRealm(book)

        // Global climate: report
        book = BookInfo()
        book.setId(StaticDefinition.global_climate_id_en.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(StaticDefinition.global_climate_title_en)
        book.setDescription(StaticDefinition.global_climate_description_en)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.global_climate_total_pages_en)
        book.setThumbNail(R.drawable.global_climate_en)
        book.setPdfName(StaticDefinition.global_climate_book_pdf_en)
        realm.copyToRealm(book)

        // Practices and meditations
        book = BookInfo()
        book.setId(StaticDefinition.practices_and_meditations_id_en.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(StaticDefinition.practices_and_meditations_title_en)
        book.setDescription(StaticDefinition.practices_and_meditations_description_en)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.practices_and_meditations_total_pages_en)
        book.setThumbNail(R.drawable.practices_and_meditations_en)
        book.setPdfName(StaticDefinition.practices_and_meditations_book_pdf_en)
        realm.copyToRealm(book)

        // Predictions and future
        book = BookInfo()
        book.setId(StaticDefinition.predictions_of_the_future_id_en.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(StaticDefinition.predictions_of_the_future_title_en)
        book.setDescription(StaticDefinition.predictions_of_the_future_description_en)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.predictions_of_the_future_total_pages_en)
        book.setThumbNail(R.drawable.predictions_of_the_future_en)
        book.setPdfName(StaticDefinition.predictions_of_the_future_book_pdf_en)
        realm.copyToRealm(book)

        // Primordial allatra physics
        book = BookInfo()
        book.setId(StaticDefinition.primordial_physics_id_en.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.EN.abbreviation)
        book.setTitle(StaticDefinition.primordial_physics_title_en)
        book.setDescription(StaticDefinition.primordial_physics_description_en)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.primordial_physics_total_pages_en)
        book.setThumbNail(R.drawable.primordial_physics_en)
        book.setPdfName(StaticDefinition.primordial_physics_book_pdf_en)
        realm.copyToRealm(book)
    }

    private fun createBooksRU() {
        var book = BookInfo()
        // sensei 1
        book.setId(StaticDefinition.sensei_1_id_ru.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.sensei_1_title_ru)
        book.setDescription(StaticDefinition.sensei_1_description_ru)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.sensei_1_total_pages_ru)
        book.setThumbNail(R.drawable.sensei_1_ru)
        book.setPdfName(StaticDefinition.sensei_1_book_pdf_ru)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.sensei_2_id_ru.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.sensei_2_title_ru)
        book.setDescription(StaticDefinition.sensei_2_description_ru)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.sensei_2_total_pages_ru)
        book.setThumbNail(R.drawable.sensei_2_ru)
        book.setPdfName(StaticDefinition.sensei_2_book_pdf_ru)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.sensei_3_id_ru.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.sensei_3_title_ru)
        book.setDescription(StaticDefinition.sensei_3_description_ru)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.sensei_3_total_pages_ru)
        book.setThumbNail(R.drawable.sensei_3_ru)
        book.setPdfName(StaticDefinition.sensei_3_book_pdf_ru)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.sensei_4_id_ru.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.sensei_4_title_ru)
        book.setDescription(StaticDefinition.sensei_4_description_ru)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.sensei_4_total_pages_ru)
        book.setThumbNail(R.drawable.sensei_4_ru)
        book.setPdfName(StaticDefinition.sensei_4_book_pdf_ru)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.allatra_id_ru.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.allatra_title_ru)
        book.setDescription(StaticDefinition.allatra_description_ru)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.allatra_total_pages_ru)
        book.setThumbNail(R.drawable.allatra_ru)
        book.setPdfName(StaticDefinition.allatra_book_pdf_ru)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.csc_id_ru.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.csc_title_ru)
        book.setDescription(StaticDefinition.csc_description_ru)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.csc_total_pages_ru)
        book.setThumbNail(R.drawable.csc_ru)
        book.setPdfName(StaticDefinition.csc_book_pdf_ru)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.birds_and_stone_id_ru.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.birds_and_stone_title_ru)
        book.setDescription(StaticDefinition.birds_and_stone_description_ru)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.birds_and_stone_total_pages_ru)
        book.setThumbNail(R.drawable.birds_and_stone_ru)
        book.setPdfName(StaticDefinition.birds_and_stone_book_pdf_ru)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.ezoosmos_id_ru.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.ezoosmos_title_ru)
        book.setDescription(StaticDefinition.ezoosmos_description_ru)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.ezoosmos_total_pages_ru)
        book.setThumbNail(R.drawable.ezoosmos_book_ru)
        book.setPdfName(StaticDefinition.ezoosmos_book_pdf_ru)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.crossroads_id_ru.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.crossroads_title_ru)
        book.setDescription(StaticDefinition.crossroads_description_ru)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.crossroads_total_pages_ru)
        book.setThumbNail(R.drawable.crossroads_book_ru)
        book.setPdfName(StaticDefinition.crossroads_book_pdf_ru)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.global_climate_id_ru.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.global_climate_title_ru)
        book.setDescription(StaticDefinition.global_climate_description_ru)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.global_climate_total_pages_ru)
        book.setThumbNail(R.drawable.global_climate_ru)
        book.setPdfName(StaticDefinition.global_climate_book_pdf_ru)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.practices_and_meditations_id_ru.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.practices_and_meditations_title_ru)
        book.setDescription(StaticDefinition.practices_and_meditations_description_ru)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.practices_and_meditations_total_pages_ru)
        book.setThumbNail(R.drawable.practices_and_meditations_ru)
        book.setPdfName(StaticDefinition.practices_and_meditations_book_pdf_ru)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.predictions_of_the_future_id_ru.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.predictions_of_the_future_title_ru)
        book.setDescription(StaticDefinition.predictions_of_the_future_description_ru)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.predictions_of_the_future_total_pages_ru)
        book.setThumbNail(R.drawable.predictions_of_the_future_ru)
        book.setPdfName(StaticDefinition.predictions_of_the_future_book_pdf_ru)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.primordial_physics_id_ru.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.primordial_physics_title_ru)
        book.setDescription(StaticDefinition.primordial_physics_description_ru)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.primordial_physics_total_pages_ru)
        book.setThumbNail(R.drawable.primordial_physics_ru)
        book.setPdfName(StaticDefinition.primordial_physics_book_pdf_ru)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.universal_grain_id_ru.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.universal_grain_title_ru)
        book.setDescription(StaticDefinition.universal_grain_description_ru)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.universal_grain_total_pages_ru)
        book.setThumbNail(R.drawable.universal_grain_ru)
        book.setPdfName(StaticDefinition.universal_grain_book_pdf_ru)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.life_source_id_ru.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.life_source_title_ru)
        book.setDescription(StaticDefinition.life_source_description_ru)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.life_source_total_pages_ru)
        book.setThumbNail(R.drawable.life_source_ru)
        book.setPdfName(StaticDefinition.life_source_book_pdf_ru)
        realm.copyToRealm(book)
    }

    private fun createBooksUA() {
        var book = BookInfo()

        book.setId(StaticDefinition.sensei_1_id_ua.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.sensei_1_title_ua)
        book.setDescription(StaticDefinition.sensei_1_description_ua)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.sensei_1_total_pages_ua)
        book.setThumbNail(R.drawable.sensei_1_ru)
        book.setPdfName(StaticDefinition.sensei_1_book_pdf_ua)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.sensei_2_id_ua.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.sensei_2_title_ua)
        book.setDescription(StaticDefinition.sensei_2_description_ua)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.sensei_2_total_pages_ua)
        book.setThumbNail(R.drawable.sensei_2_ru)
        book.setPdfName(StaticDefinition.sensei_2_book_pdf_ua)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.allatra_id_ua.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.allatra_title_ua)
        book.setDescription(StaticDefinition.allatra_description_ua)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.allatra_total_pages_ua)
        book.setThumbNail(R.drawable.sensei_2_ru)
        book.setPdfName(StaticDefinition.allatra_book_pdf_ua)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.birds_and_stone_id_ua.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.birds_and_stone_title_ua)
        book.setDescription(StaticDefinition.birds_and_stone_description_ua)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.birds_and_stone_total_pages_ua)
        book.setThumbNail(R.drawable.sensei_2_ru)
        book.setPdfName(StaticDefinition.birds_and_stone_book_pdf_ua)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.global_climate_id_ua.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.global_climate_title_ua)
        book.setDescription(StaticDefinition.global_climate_description_ua)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.global_climate_total_pages_ua)
        book.setThumbNail(R.drawable.sensei_2_ru)
        book.setPdfName(StaticDefinition.global_climate_book_pdf_ua)
        realm.copyToRealm(book)

        book.setId(StaticDefinition.primordial_physics_id_ua.toLong())
        book.setLanguage(EnumDefinition.EnLanguage.RU.abbreviation)
        book.setTitle(StaticDefinition.primordial_physics_title_ua)
        book.setDescription(StaticDefinition.primordial_physics_description_ua)
        book.setIndexPage(0)
        book.setTotalPages(StaticDefinition.primordial_physics_total_pages_ua)
        book.setThumbNail(R.drawable.sensei_2_ru)
        book.setPdfName(StaticDefinition.primordial_physics_book_pdf_ua)
        realm.copyToRealm(book)
    }

    fun getFilteredBooks(language: EnumDefinition.EnLanguage): MutableList<BookInfo> {
        return listOfBooks.filter { bookInfo -> bookInfo.getLanguageEnum().equals(language) }.sortedBy { bookInfo -> bookInfo.getId() }.toMutableList()
    }
}