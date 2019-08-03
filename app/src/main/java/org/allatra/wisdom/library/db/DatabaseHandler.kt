package org.allatra.wisdom.library.db

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.delete
import org.allatra.wisdom.library.R
import org.allatra.wisdom.library.static.EnumDefinition
import org.allatra.wisdom.library.static.StaticDefinition
import org.readium.r2.shared.Publication
import org.readium.r2.streamer.parser.PubBox
import timber.log.Timber

class DatabaseHandler() {
    private lateinit var realm: Realm
    private lateinit var context: Context

    companion object {
        private const val TAG = "DB"
    }

    constructor(context: Context) : this() {
        this.context = context
        initRealm()
    }

    private fun initRealm(){
        // Initialize Realm
        Realm.init(context)
        val config = RealmConfiguration.Builder()
            .name(StaticDefinition.app_realm_name)
            .schemaVersion(3)
            .deleteRealmIfMigrationNeeded()
            .build()
        //dropRealm(config)
        realm = Realm.getInstance(config)
    }

    fun getListOfBookInfo(language: String): MutableList<BookInfo>{
        Log.i(TAG, "Language is $language")
        //deleteAllRecords()
        // Init user settings
        initUserSettings(language)
        // Try to fetch existing records
        var listOfBooks = realm.where(BookInfo::class.java).findAll()

        if(listOfBooks.size==0){
            Log.i(TAG, "Db was initialized with new records.")
            realm.beginTransaction()
            realm.commitTransaction()

            listOfBooks = realm.where(BookInfo::class.java).findAll()
        } else {
            Log.i(TAG, "Db has records. Size: ${listOfBooks.size}")
        }

        return listOfBooks
    }

    private fun dropRealm(realmConfiguration: RealmConfiguration){
        Realm.deleteRealm(realmConfiguration)
    }

    private fun deleteAllRecords(){
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()
    }

   private fun initUserSettings(language: String){
        var userSettings = UserSettings()
        userSettings.setId(0)
        userSettings.setLanguage(language)
    }

    fun getBookInfoById(id: Long): BookInfo?{
        return realm.where(BookInfo::class.java).equalTo("id", id).findFirst()
    }

    fun updateBookInfo(bookInfo: BookInfo, indexPage: Int){
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(bookInfo)
        realm.commitTransaction()
    }

    fun createBookInfoObject(pubBox: PubBox){
        val publication = pubBox.publication
        val container = pubBox.container

        if (publication.type == Publication.TYPE.EPUB) {
            val publicationIdentifier = publication.metadata.identifier


        } else {
            Timber.i("It is not a ePub publication.")
        }
    }
}