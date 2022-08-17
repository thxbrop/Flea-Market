package com.linku.data

import android.util.Log
import com.linku.domain.BuildConfig

val <T> T.TAG: String
    get() = "[LinkU]" + this!!::class.java.simpleName

inline fun <R> debug(block: () -> R): R? {
    return try {
        if (BuildConfig.DEBUG) block()
        else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

inline fun <R> release(block: () -> R): R? {
    return try {
        if (!BuildConfig.DEBUG) block()
        else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun error(message: String?, tag: String = "Error") {
    debug {
        Log.e(tag, message ?: "")
    }
}