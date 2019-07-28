package org.allatra.wisdom.library.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.allatra.wisdom.library.static.EnumDefinition.EnLanguage
import org.allatra.wisdom.library.static.EnumDefinition.EnLayout

open class UserSettings: RealmObject() {
    @PrimaryKey
    private var id: Long = 0

    private var language: String? = EnLanguage.EN.abbreviation

    private var layout: String = EnLayout.LINEAR.abbreviation

    fun getId(): Long = id
    fun getLanguage(): String? = language
    fun getLayout(): String? = layout

    fun setId(id: Long){
        this.id = id
    }

    fun setLanguage(language: String){
        this.language = language
    }

    fun setLayout(layout: String){
        this.layout = layout
    }
}