package com.degstu.quickmoneycounter

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
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
        buttonUndo.setOnClickListener { undo() }

        load()
        constructUI()
    }

    private fun calc() {
        val f = DecimalFormat("###,##0.00")
        labelTotal.text = currency.masterSymbol + f.format(currency.sum())
    }

    private fun reset() {
        fun r() {
            //load default
            currency = CurrencyList().getCurrency(Settings.getSetting("activeCurrency")!!.loadValue(this))
            //write
            currency.write(this)

            //load from write
            load()
        }

        if (currency.sumOps() >= Currency.RESET_CONFIRM_OPS_COUNT) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.confirm_reset_title)
            builder.setMessage(R.string.confirm_reset_message)

            val click = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> r()
                    DialogInterface.BUTTON_NEGATIVE -> toast(resources.getString(R.string.confirm_reset_cancelled))
                }
            }

            builder.setPositiveButton(R.string.button_yes, click)
            builder.setNegativeButton(R.string.button_no, click)

            builder.create().show()
        } else {
            r()
        }
    }

    private fun undo() {
        val undo: String = currency.undo()

        if (undo != "") {
            toast(resources.getString(R.string.undo_success) + " " + undo)
        } else {
            toast(resources.getString(R.string.undo_fail))
        }

        calc()
    }

    private fun load() {
        //load active currency
        currency = CurrencyList().getCurrency(Settings.getSetting("activeCurrency")!!.loadValue(this))
        currency.load(this)

        //load mode (not currently in use)
        mode = Settings.getSetting("mode")!!.loadValue(this)

        //run calc to set the sum label to 0.00
        calc()
    }

    private fun constructUI() {
        for (m: MoneyPiece in currency.paperCommon) {
            addButton(m, layoutPaperCommonButtons)
        }
        for (m: MoneyPiece in currency.paperUncommon) {
            addButton(m, layoutPaperUncommonButtons)
        }
        for (m: MoneyPiece in currency.coins) {
            addButton(m, layoutCoinsButtons)
        }
    }

    private fun addButton(m: MoneyPiece, l: LinearLayout) {
        val b: Button = Button(this)
        b.text = m.display
        b.setOnClickListener {
            currency.increment(m.uniqueIdentifier)
            calc()
            currency.write(this)
        }

        l.addView(b)
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
