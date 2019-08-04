package org.allatra.wisdom.library.db

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
import org.allatra.wisdom.library.static.EnumDefinition
import org.allatra.wisdom.library.static.StaticDefinition
import org.readium.r2.shared.Publication
import org.readium.r2.streamer.parser.PubBox
import org.zeroturnaround.zip.ZipUtil
import timber.log.Timber
import java.io.File

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

    fun getListOfBookInfoANdInit(language: String): MutableList<BookInfo>{
        Timber.i( "Language is $language")
        //deleteAllRecords()
        // Init user settings
        initUserSettings(language)
        // Try to fetch existing records
        var listOfBooks = realm.where(BookInfo::class.java).findAll()

        if(listOfBooks.size==0){
            Timber.i("Db was initialized with new records.")
            realm.beginTransaction()
            realm.commitTransaction()

            listOfBooks = realm.where(BookInfo::class.java).findAll()
        } else {
            Timber.i( "Db has records. Size: ${listOfBooks.size}")
        }

        return listOfBooks
    }

    fun getListOfBookInfo(): MutableList<BookInfo>{
        return realm.where(BookInfo::class.java).findAll()
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

    fun getBookInfoByPublicationIdentifier(identifier: String): BookInfo?{
        return realm.where(BookInfo::class.java).equalTo("identifier", identifier).findFirst()
    }

    fun updateBookInfo(bookInfo: BookInfo, indexPage: Int){
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(bookInfo)
        realm.commitTransaction()
    }

    fun createBookInfoObject(pubBox: PubBox, fileUUID: String, absolutePath: String, enLanguage: EnumDefinition.EnLanguage){
        if(!realm.isInTransaction){
            Timber.i("Begin transaction.")
            realm.beginTransaction()
        }
        val publication = pubBox.publication

        if (publication.type == Publication.TYPE.EPUB) {
            val publicationIdentifier = publication.metadata.identifier

            val bookInfo = BookInfo()
            bookInfo.setId(getNextLongId())
            bookInfo.setTitle(publication.metadata.title)
            bookInfo.setAuthor(getAuthorName(publication))
            bookInfo.setIdentifier(publicationIdentifier)
            bookInfo.setFileName(fileUUID)
            bookInfo.setFileAbsolutePath(absolutePath)
            publication.coverLink?.href?.let {
                bookInfo.setCoverLink(it)
                val blob = ZipUtil.unpackEntry(File(absolutePath), it.removePrefix("/"))
                blob?.let {
                    bookInfo.setCover(blob)
                }
            }
            bookInfo.setExtension(Publication.EXTENSION.EPUB.toString())
            bookInfo.setLanguage(getPublicationLanguage(publication.metadata.languages, enLanguage))
            realm.copyToRealmOrUpdate(bookInfo)
            Timber.d("Book: $bookInfo, has been added.")
        } else {
            Timber.i("It is not an ePub publication.")
        }
    }

    private fun getPublicationLanguage(languages: MutableList<String>, enLanguage: EnumDefinition.EnLanguage): String{
        var languageAbbreviation = enLanguage.abbreviation
        Timber.i("Languages: $languages")
        if(languages.isNotEmpty()) {
            val pubLanguage = languages[0]
            //TODO: implement list of the possible languages and compare
        }

        return languageAbbreviation
    }

    private fun getNextLongId(): Long{
        val currentIdNum = realm.where(BookInfo::class.java).max("id")
        val nextId: Long
        if (currentIdNum == null) {
            nextId = 1
        } else {
            nextId = currentIdNum.toLong() + 1
        }

        return nextId
    }

    fun commitRecords(){
        if(!realm.isInTransaction){
            Timber.e("Cannot commit realm is not in transaction.")
        } else {
            realm.commitTransaction()
            Timber.d("Realm committed.")
        }
    }

    private fun getAuthorName(publication: Publication): String {
        return publication.metadata.authors.firstOrNull()?.name?.let {
            return@let it
        } ?: run {
            return@run String()
        }
    }
}