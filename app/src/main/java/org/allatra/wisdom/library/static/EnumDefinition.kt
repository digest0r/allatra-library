package org.allatra.wisdom.library.static

object EnumDefinition {
    enum class EnLanguage(val abbreviation: String){
        EN("en"), RU("ru"), UA("ua"), CS("cs"), BG("bg"), DE("de"), PL("pl"), LT("lt"), IT("it")
    }

    enum class EnLanguageDesc(val description: String){
        EN("English"), RU("Русский"), UA("Українська"), CZ("Česky"), BG("Български"), DE("Deutsch"), PL("Polski"), LT("Latviešu"), IT("Italiano")
    }

    enum class EnLayout(val abbreviation: String){
        LINEAR("linear")
    }
}