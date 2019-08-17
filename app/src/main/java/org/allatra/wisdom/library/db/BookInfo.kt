package org.allatra.wisdom.library.db

import org.allatra.wisdom.library.static.EnumDefinition

data class BookInfo(
    val id: Long?,
    val fileName: String?,
    val fileUUID: String?,
    var title: String?,
    var author: String?,
    var fileAbsolutePath: String?,
    var coverLink: String?,
    var identifier: String?,
    var cover: ByteArray?,
    var extension: String?,
    var creationDate:Long?,
    var language: String?
) {
    fun getLanguageEnum(): EnumDefinition.EnLanguage {
        return EnumDefinition.EnLanguage.valueOf(language!!.toUpperCase())
    }
}