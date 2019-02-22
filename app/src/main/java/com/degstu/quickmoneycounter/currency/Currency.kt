package com.degstu.quickmoneycounter.currency

import android.content.Context

class Currency(
    val uniqueIdentifier: String,
    val name: String,
    val masterSymbol: String,
    val paperCommon: Array<MoneyPiece>,
    val paperUncommon: Array<MoneyPiece>,
    val coins: Array<MoneyPiece>
) {
    fun sum(): Double {
        var sum: Double = 0.0

        for (m: MoneyPiece in paperCommon + paperUncommon + coins) sum += m.numericValue * m.count

        return sum
    }

    fun load(context: Context) {
        for (i in paperCommon.indices) paperCommon[i].loadValue(context)
        for (i in paperUncommon.indices) paperUncommon[i].loadValue(context)
        for (i in coins.indices) coins[i].loadValue(context)
    }

    fun write(context: Context) {
        for (i in paperCommon.indices) paperCommon[i].writeValue(context)
        for (i in paperUncommon.indices) paperUncommon[i].writeValue(context)
        for (i in coins.indices) coins[i].writeValue(context)
    }

    fun increment(uniqueIdentifier: String) {
        for (i in paperCommon.indices) if (paperCommon[i].uniqueIdentifier == uniqueIdentifier) {
            paperCommon[i].count++
        }
        for (i in paperUncommon.indices) if (paperUncommon[i].uniqueIdentifier == uniqueIdentifier) {
            paperUncommon[i].count++
        }
        for (i in coins.indices) if (coins[i].uniqueIdentifier == uniqueIdentifier) {
            coins[i].count++
        }
    }
}