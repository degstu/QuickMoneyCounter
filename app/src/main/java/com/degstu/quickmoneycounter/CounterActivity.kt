package com.degstu.quickmoneycounter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout
import com.degstu.quickmoneycounter.currency.Currency
import com.degstu.quickmoneycounter.currency.CurrencyList
import com.degstu.quickmoneycounter.currency.MoneyPiece
import com.degstu.quickmoneycounter.settings.Settings
import kotlinx.android.synthetic.main.activity_counter.*
import java.text.DecimalFormat

class CounterActivity : AppCompatActivity() {
    private var currency: Currency = Currency("ERR", "ERR", "ERR", arrayOf(), arrayOf(), arrayOf())
    private var mode: String = "basic"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        buttonReset.setOnClickListener { reset() }

        load()
        constructUI()
    }

    private fun calc() {
        val f = DecimalFormat("###,##0.00")
        labelTotal.text = currency.masterSymbol + f.format(currency.sum())
    }

    private fun reset() {
        //load default
        currency = CurrencyList().getCurrency(Settings.getSetting("activeCurrency")!!.loadValue(this))
        //write
        currency.write(this)

        //load from write
        load()
    }

    private fun load() {
        //load active currency
        currency = CurrencyList().getCurrency(Settings.getSetting("activeCurrency")!!.loadValue(this))
        currency.load(this)

        //load mode (not currently in use)
        mode = Settings.getSetting("mode")!!.loadValue(this)

        //run calc to set the sum label
        calc()
    }

    private fun constructUI() {
        //TODO: this
    }

    private fun addButton(moneyPiece: MoneyPiece, layout: LinearLayout){
        val b: Button = Button(this)
        //TODO: add button to layout
        //TODO: UNIT TESTS!!!!
    }
}