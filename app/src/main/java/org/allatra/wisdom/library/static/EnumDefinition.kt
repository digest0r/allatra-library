package org.allatra.wisdom.library.static

object EnumDefinition {
    enum class EnLanguage(val abbreviation: String){
        EN("en"), RU("ru"), UA("ua"), CZ("cz"), BG("bg"), DE("de"), PL("pl"), LT("lt"), IT("it")
    }

    enum class EnLanguageDesc(val description: String){
        EN("English"), RU("Русский"), UA("Українська"), CZ("Česky"), BG("Български"), DE("Deutsch"), PL("Polski"), LT("Latviešu"), IT("Italiano")
    }
}