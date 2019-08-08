package org.allatra.wisdom.library.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.allatra.wisdom.library.static.EnumDefinition
import org.allatra.wisdom.library.static.EnumDefinition.EnLanguage
import org.joda.time.DateTime
import org.readium.r2.shared.Publication

open class BookInfoDAO: RealmObject() {
    @PrimaryKey
    private var id: Long? = null

    private var fileName: String? = null

    private var title: String? = null

    private var author: String? = null

    private var fileAbsolutePath: String? = null

    private var coverLink: String? = null

    private var identifier: String? = null

    private var cover: ByteArray? = null

    private var extension: String = Publication.EXTENSION.EPUB.value

    private var creationDate:Long = DateTime().toDate().time

    private var language: String = EnLanguage.EN.abbreviation

    fun getId(): Long? = id
    fun getTitle(): String? = title
    fun getIdentifier(): String? = identifier
    fun getCover(): ByteArray? = cover
    fun getCoverLink(): String? = coverLink
    fun getExtension(): String? = extension
    fun getCreationDate(): Long? = creationDate
    fun getLanguage(): String? = language
    fun getFileName(): String? = fileName
    fun getAuthor(): String? = author
    fun getFileAbsolutePath(): String? = fileAbsolutePath

    fun getLanguageEnum(): EnLanguage {
        return EnumDefinition.EnLanguage.valueOf(language.toUpperCase())
    }

    fun setId(id: Long){
        this.id = id
    }

    fun setFileName(fileName: String){
        this.fileName = fileName
    }

    fun setAuthor(author: String){
        this.author = author
    }

    fun setFileAbsolutePath(fileAbsolutePath: String){
        this.fileAbsolutePath = fileAbsolutePath
    }

    fun setCoverLink(coverLink: String){
        this.coverLink = coverLink
    }

    fun setIdentifier(identifier: String){
        this.identifier = identifier
    }

    fun setCover(cover: ByteArray){
        this.cover = cover
    }

    fun setExtension(extension: String){
        this.extension = extension
    }

    fun setcreationDate(creationDate: Long){
        this.creationDate = creationDate
    }

    fun setLanguage(language: String){
        this.language = language
    }

    fun setTitle(title: String){
        this.title = title
    }
}