package com.degstu.quickmoneycounter.currency

import android.content.Context
import android.content.SharedPreferences

class Currency(
    val uniqueIdentifier: String,
    val name: String,
    val masterSymbol: String,
    val paperCommon: Array<MoneyPiece>,
    val paperUncommon: Array<MoneyPiece>,
    val coins: Array<MoneyPiece>
) {
    companion object {
        const val RESET_CONFIRM_OPS_COUNT = 5
    }

    private var opList: MutableList<String> = mutableListOf()
    private val OP_LIST_PREF_NAME: String = uniqueIdentifier + "_OPLIST"

    fun sum(): Double {
        var sum: Double = 0.0

        for (m: MoneyPiece in paperCommon + paperUncommon + coins) sum += m.numericValue * m.count

        return sum
    }

    fun sumOps(): Int {
        var sum = 0

        for (m in paperCommon + paperUncommon + coins) sum += m.count

        return sum
    }

    fun undo(undoLatestMoneyPiece: String = ""): String {
        if (opList.isNotEmpty()) {
            val i = if (undoLatestMoneyPiece == "") opList.size - 1 else opList.lastIndexOf(undoLatestMoneyPiece)
            if (i == -1) return ""

            increment(opList[i], false)

            val rem = opList[i]
            opList.removeAt(i)

            for (m: MoneyPiece in paperCommon + paperUncommon + coins)
                if (m.uniqueIdentifier == rem)
                    return m.display

        }

        return ""
    }

    fun load(context: Context) {
        for (i in paperCommon.indices) paperCommon[i].loadValue(context)
        for (i in paperUncommon.indices) paperUncommon[i].loadValue(context)
        for (i in coins.indices) coins[i].loadValue(context)

        //operation list
        val pref: SharedPreferences = context.getSharedPreferences(MoneyPiece.PREF_FILE_MONEY, Context.MODE_PRIVATE)
        opList = pref.getString(OP_LIST_PREF_NAME, "")!!.split("|").toMutableList()
    }

    fun write(context: Context) {
        for (i in paperCommon.indices) paperCommon[i].writeValue(context)
        for (i in paperUncommon.indices) paperUncommon[i].writeValue(context)
        for (i in coins.indices) coins[i].writeValue(context)

        //operation list
        val pref: SharedPreferences.Editor =
            context.getSharedPreferences(MoneyPiece.PREF_FILE_MONEY, Context.MODE_PRIVATE).edit()
        pref.putString(OP_LIST_PREF_NAME, opList.joinToString("|"))
        pref.apply()
    }

    fun increment(uniqueIdentifier: String, add: Boolean = true) {
        val amount = if (add) 1 else -1

        for (i in paperCommon.indices) if (paperCommon[i].uniqueIdentifier == uniqueIdentifier) paperCommon[i].count += amount
        for (i in paperUncommon.indices) if (paperUncommon[i].uniqueIdentifier == uniqueIdentifier) paperUncommon[i].count += amount
        for (i in coins.indices) if (coins[i].uniqueIdentifier == uniqueIdentifier) coins[i].count += amount

        if (add) opList.add(uniqueIdentifier)
    }
}