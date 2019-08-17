package org.allatra.wisdom.library

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.*

//


open class LocaleManager{

    companion object {
        private const val KEY_LANG = "Key_language"
    }

    fun changeLanguage(mContext: Context, language: String, save: Boolean=true):Context{
        var res: Resources = mContext.resources
        // create the corresponding locale
        var locale: Locale = Locale(language); // for example "en"
        // Change locale settings in the app.
        var conf: Configuration = res.configuration

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(locale)
            conf.setLayoutDirection(locale)
        } else {
            @Suppress("DEPRECATION")
            conf.locale = locale
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(mContext, language)
        }
        @Suppress("DEPRECATION")
        res.updateConfiguration(conf, null)

        if (save){
            var mSharedPref: SharedPreferences = mContext.getSharedPreferences(KEY_LANG, Context.MODE_PRIVATE)
            mSharedPref.edit().putString(KEY_LANG,language).commit()
        }
        return mContext
    }

    fun loadSharedResources(mContext: Context, debug: Boolean=false):String?{
        var mSharedPref: SharedPreferences = mContext.getSharedPreferences(KEY_LANG, Context.MODE_PRIVATE)
        changeLanguage(mContext, mSharedPref.getString(KEY_LANG, "")!!, false)
        if (debug){
            return mSharedPref.getString(KEY_LANG, "")
        }
        return ""
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

}