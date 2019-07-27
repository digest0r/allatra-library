package org.allatra.wisdom.library.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.allatra.wisdom.library.static.EnumDefinition
import org.allatra.wisdom.library.static.EnumDefinition.EnLanguage

open class BookInfo: RealmObject() {
    @PrimaryKey
    private var id: Long = 0

    private var title: String? = null

    private var language: String = EnLanguage.EN.abbreviation

    private var pdfName: String? = null

    private var description: String? = null

    private var indexPage: Int = 0

    private var totalPages: Int = 0

    private var thumbNail: Int = 0

    fun getId(): Long = id
    fun getTitle(): String? = title
    fun getPdfName(): String? = pdfName
    fun getDescription(): String? = description
    fun getIndexPage(): Int = indexPage
    fun getTotalPages(): Int = totalPages
    fun getThumbNail(): Int = thumbNail
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

    fun setPdfName(pdfName: String){
        this.pdfName = pdfName
    }

    fun setDescription(description: String){
        this.description = description
    }

    fun setIndexPage(indexPage: Int){
        this.indexPage = indexPage
    }

    fun setTotalPages(totalPages: Int){
        this.totalPages = totalPages
    }

    fun setThumbNail(thumbNail: Int){
        this.thumbNail = thumbNail
    }
}