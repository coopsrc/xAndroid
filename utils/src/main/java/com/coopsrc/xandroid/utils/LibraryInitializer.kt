package com.coopsrc.xandroid.utils

import android.content.Context
import androidx.startup.Initializer

/**
 * @author Tingkuo
 *
 *
 * Datetime: 2020-09-14 16:56
 */
class LibraryInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        return ContextProvider.initContext(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>> {
        return emptyList()
    }
}