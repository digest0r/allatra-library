package org.allatra.wisdom.library.db

import android.content.Context
import androidx.core.os.LocaleListCompat
import io.realm.Realm
import io.realm.RealmConfiguration
import org.allatra.wisdom.library.static.EnumDefinition
import org.allatra.wisdom.library.static.StaticDefinition
import org.allatra.wisdom.library.ui.activity.MainActivity
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
        private const val DEFAULT_USER_ID = 1L
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

   fun initUserSettingsAndReturn(localeListCompat: LocaleListCompat): EnumDefinition.EnLanguage{
       val user = realm.where(UserSettings::class.java).equalTo("id", DEFAULT_USER_ID).findFirst()
       var userSettings = UserSettings()
       var enLanguage : EnumDefinition.EnLanguage
       if(user == null){
           Timber.tag(TAG).i("User has no preferred settings yet. Creating new one.")
           userSettings = UserSettings()
           userSettings.setId(DEFAULT_USER_ID)

           // Actual user language
           val actualLang = localeListCompat[0]
           if(localeListCompat.size()>1){
               Timber.tag(TAG).w("User has more languages installed. $localeListCompat")
           }

           Timber.tag(TAG).i("User has main language set to: ${actualLang.language}, ${actualLang.displayLanguage}")
           // Parse the language
           try{
                enLanguage = EnumDefinition.EnLanguage.valueOf(actualLang.language.toUpperCase())
           } catch (e: java.lang.IllegalArgumentException){
               Timber.tag(TAG).e("Language of user cannot be parsed: ${actualLang.language}, ${actualLang.displayLanguage}.")
               enLanguage = EnumDefinition.EnLanguage.EN
               userSettings.setLanguage(enLanguage.abbreviation)
           }

           realm.beginTransaction()
           realm.copyToRealmOrUpdate(userSettings)
           realm.commitTransaction()
       } else {
           Timber.tag(TAG).d("User settings exist.")
           enLanguage = EnumDefinition.EnLanguage.valueOf(user.getLanguage()!!.toUpperCase())
       }

       return enLanguage
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
            var enLanguage: EnumDefinition.EnLanguage
            try{
                enLanguage = EnumDefinition.EnLanguage.valueOf(pubLanguage.toUpperCase())
                languageAbbreviation = enLanguage.abbreviation
            } catch (e: java.lang.IllegalArgumentException){
                Timber.tag(TAG).e("Language of publication cannot be parsed: $pubLanguage.")
            }
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