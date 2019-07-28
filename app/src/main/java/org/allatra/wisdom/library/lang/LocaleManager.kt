package org.allatra.wisdom.library.lang

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

class LocaleManager {

    fun setLocale(context: Context, language: String): Context?{
        return setLocaleInner(context, language)
    }

    private fun setLocaleInner(context: Context, language: String): Context?{
        return updateResources(context, language)
    }

    private fun updateResources(context: Context, language: String): Context?{
        //TODO: For different SDK used
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)

        when{
            Build.VERSION.SDK_INT < 17 -> {
                config.locale = locale
                res.updateConfiguration(config, res.displayMetrics)
            }

            Build.VERSION.SDK_INT >= 17 -> {
                config.setLocale(locale)
                return context.createConfigurationContext(config)
            }
        }

        return null
    }

}