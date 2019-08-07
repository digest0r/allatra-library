package org.allatra.wisdom.library.lang

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(contex: Context){

    private final val KEY_FLAG = "language"
    val sharedPref: SharedPreferences = contex.getSharedPreferences(KEY_FLAG, Context.MODE_PRIVATE)

    fun save(key_name: String,value: String ){
        val pref: SharedPreferences.Editor = sharedPref.edit()
        pref.putString(key_name, value)
        pref.commit()
    }

    fun getValueLang(key_name: String): String?{
        return sharedPref.getString(key_name, null)
    }

    fun clearValueLang(){
        val pref: SharedPreferences.Editor = sharedPref.edit()
        pref.clear()
        pref.commit()
    }

    fun removeLanguage(key_name: String){
        val pref: SharedPreferences.Editor = sharedPref.edit()
        pref.remove(key_name)
        pref.commit()
    }
}