package com.degstu.quickmoneycounter.settings

import android.content.Context
import android.content.SharedPreferences

class Setting(val uniqueIdentifier: String, val default: String) {
    companion object {
        const val PREF_FILE_CONFIG = "config"
    }

    var value: String = default

    fun loadValue(context: Context): String {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_FILE_CONFIG, Context.MODE_PRIVATE)
        value = pref.getString(uniqueIdentifier, default) as String
        return value
    }

    fun writeValue(context: Context, value: String) {
        val pref: SharedPreferences.Editor = context.getSharedPreferences(PREF_FILE_CONFIG, Context.MODE_PRIVATE).edit()
        pref.putString(uniqueIdentifier, value)
        pref.apply()
    }
}