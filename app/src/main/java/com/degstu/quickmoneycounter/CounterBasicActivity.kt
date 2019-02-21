package com.degstu.quickmoneycounter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import com.degstu.quickmoneycounter.currency.Currency
import com.degstu.quickmoneycounter.currency.CurrencyList
import com.degstu.quickmoneycounter.currency.MoneyPiece
import com.degstu.quickmoneycounter.settings.Setting
import com.degstu.quickmoneycounter.settings.Settings
import kotlinx.android.synthetic.main.activity_counter_basic.*

class CounterBasicActivity : AppCompatActivity() {
    private var currency: Currency = Currency("ERR", "ERR", "ERR", arrayOf(), arrayOf(), arrayOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter_basic)

        currency = CurrencyList().getCurrency((Settings.getSetting("activeCurrency") as Setting).loadValue(this))
        //TODO: set total with decimal formatter, add sum function to Currency, figure out coin button text solution

        for(m: MoneyPiece in currency.paperCommon){
            addButton(m, layoutBasicPaperCommon)
        }
        for(m: MoneyPiece in currency.paperUncommon){
            addButton(m, layoutBasicPaperUncommon)
        }
        for(m: MoneyPiece in currency.coins){
            addButton(m, layoutBasicCoinCommon)
        }
    }

    private fun addButton(moneyPiece: MoneyPiece, layout: LinearLayout){
        val b: Button = Button(this)
        //TODO: add button to layout
    }
}
