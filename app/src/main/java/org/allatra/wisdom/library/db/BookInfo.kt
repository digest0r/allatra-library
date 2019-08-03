package org.allatra.wisdom.library.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.allatra.wisdom.library.static.EnumDefinition
import org.allatra.wisdom.library.static.EnumDefinition.EnLanguage
import org.joda.time.DateTime
import org.readium.r2.shared.Publication

open class BookInfo: RealmObject() {
    @PrimaryKey
    private var id: Long = 0

    private var fileName: String? = null

    private var title: String? = null

    private val author: String? = null

    private val fileUrl: String? = null

    private val coverLink: String? = null

    private val identifier: String? = null

    private val cover: ByteArray? = null

    private val ext: Publication.EXTENSION = Publication.EXTENSION.EPUB

    private val creation:Long = DateTime().toDate().time

    private var language: String = EnLanguage.EN.abbreviation

    fun getId(): Long = id
    fun getTitle(): String? = title

    fun getLanguage(): String = language
    fun getLanguageEnum(): EnLanguage {
        return EnumDefinition.EnLanguage.valueOf(language.toUpperCase())
    }

    fun setId(id: Long){
        this.id = id
    }

    fun setLanguage(language: String){
        this.language = language
    }

    fun setTitle(title: String){
        this.title = title
    }
}