package com.degstu.quickmoneycounter.currency

import android.content.Context
import android.content.SharedPreferences

open class MoneyPiece(
    val uniqueIdentifier: String,
    val numericValue: Double,
    val symbol: String,
    val symbolFirst: Boolean = true,
    var count: Int = 0
) {
    companion object {
        const val PREF_FILE_MONEY: String = "money"
    }

    fun loadValue(context: Context) {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_FILE_MONEY, Context.MODE_PRIVATE)
        val prefVal: Int = pref.getInt(uniqueIdentifier, 0)
        count = prefVal
    }

    fun writeValue(context: Context) {
        val pref: SharedPreferences.Editor = context.getSharedPreferences(PREF_FILE_MONEY, Context.MODE_PRIVATE).edit()
        pref.putInt(uniqueIdentifier, count)
        pref.apply()
    }
}