package org.allatra.wisdom.library

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.*

//


open class LocaleManager(){

    val KEY_LANG = "Key_language"

    fun changeLanguage(mContext: Context, language: String, save: Boolean=true):Context{
        var res: Resources = mContext.resources;
        // create the corresponding locale
        var locale: Locale = Locale(language); // for example "en"
        // Change locale settings in the app.
        var conf: Configuration = res.configuration;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(locale);
            conf.setLayoutDirection(locale);
        } else {
            @Suppress("DEPRECATION")
            conf.locale = locale;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(mContext, language)
        }
        @Suppress("DEPRECATION")
        res.updateConfiguration(conf, null);

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

    fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

}
//import android.content.Context
//import android.content.SharedPreferences
//import android.content.res.Configuration
//import android.content.res.Resources
//import android.os.Build
//import java.util.*
//
//
//open class LocaleManager(){
//    companion object {
//        val LANGUAGE_KEY = "Language_Key"
//
//        val LANGUAGE_KEY_ENGLISH: String = "en"
//        /**
//         * for hindi locale
//         */
//        val LANGUAGE_KEY_Ukraine = "uk"
//        /***
//         * // for spanish locale
//         */
//
//        val LANGUAGE_KEY_Czech = "cz"
//
//        /**
//         * SharedPreferences Key
//         */
//
//        /* set default locale*/
//        fun setLocale(mContext: Context):Context  {
//            return updatedResources(mContext, getLanguagePref(mContext))
//        }
//
//        /*  set new locale */
//        fun setNewLocale(mContext: Context, mLocale_key: String): Context {
//            setLanguagePref(mContext, mLocale_key)
//            return updatedResources(mContext, mLocale_key)
//        }
//
//        /* Get saved locale from preferences */
//
//        fun getLanguagePref(mContext: Context): String{
//            var LANGUAGE_KEY_ENGLISH = "en"
//            var mSharedPref: SharedPreferences = mContext.getSharedPreferences(LANGUAGE_KEY, Context.MODE_PRIVATE)
//            return mSharedPref.getString(LANGUAGE_KEY,  "en" )!!
//        }
//
//        /* set pref. key for locale   */
//        fun setLanguagePref(mContext: Context, locale_key: String) {
//            var mEditor: SharedPreferences.Editor = mContext.getSharedPreferences(LANGUAGE_KEY, Context.MODE_PRIVATE).edit()
//            mEditor.putString(LANGUAGE_KEY, locale_key)
//            mEditor.apply()
//
//        }
//
//        @Suppress("DEPRECATION")
//        fun updatedResources(mContext: Context, mLanguage: String): Context {
//            var mLocale: Locale = Locale(mLanguage)
//            Locale.setDefault(mLocale)
//            var mRes: Resources = mContext.resources
//            var mConfig: Configuration = Configuration(mRes.configuration)
//
//            if (Build.VERSION.SDK_INT >= 24) {
//                mConfig.locales.get(0)
//
//            } else if ((Build.VERSION.SDK_INT >= 17) && (Build.VERSION.SDK_INT <= 24)) {
//                mConfig.setLocale(mLocale)
//                mContext.createConfigurationContext(mConfig)
//            } else if (Build.VERSION.SDK_INT <= 17) {
//                mConfig.locale = mLocale
//                mRes.updateConfiguration(mConfig, mRes.displayMetrics)
//            }
//            return mContext
//        }
//
//        @Suppress("DEPRECATION")
//        fun getLocale(res: Resources): Locale {
//            val config = res.configuration
//            return if (Build.VERSION.SDK_INT >= 24) config.locales.get(0) else config.locale
//        }
//
//    }
//}