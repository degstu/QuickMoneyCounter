package com.degstu.quickmoneycounter

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.degstu.quickmoneycounter.currency.Currency
import com.degstu.quickmoneycounter.currency.CurrencyList
import com.degstu.quickmoneycounter.currency.MoneyPiece
import com.degstu.quickmoneycounter.settings.Settings
import kotlinx.android.synthetic.main.activity_counter.*
import java.text.DecimalFormat

class CounterActivity : AppCompatActivity() {
    private var currency: Currency = Currency("ERR", "ERR", "ERR", arrayOf(), arrayOf(), arrayOf())
    private var mode: String = Settings.Modes.BASIC.value
    private var advancedModeEditText: MutableList<EditText> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        if (Settings.getSetting("eula")!!.loadValue(this) != "true") {
            startActivityForResult(Intent(this, EulaActivity::class.java), 1)
        }

        buttonReset.setOnClickListener { reset() }
        buttonSettings.setOnClickListener {
            val settingsIntent: Intent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(settingsIntent, 2)
        }
        buttonUndo.setOnClickListener { undo() }

        load()
        constructUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (Settings.getSetting("eula")!!.loadValue(this) != "true") {
                finish()
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                //reload CounterActivity in case the user change the mode in settings
                finish()
                startActivity(intent)
            }
        }
    }

    private fun calc() {
        val sum: Double = currency.sum()
        val f = DecimalFormat("###,##0.00")

        labelTotal.text = currency.masterSymbol + f.format(sum)

        if (mode == Settings.Modes.ADVANCED.value) {
            for ((m, e) in (currency.paperCommon + currency.paperUncommon + currency.coins).zip(advancedModeEditText).toMap()) {
                if (!e.hasFocus()) {
                    setEditTextText(e, m.count.toString())
                }
            }
        }
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

        if (currency.sumOps() >= Currency.RESET_CONFIRM_OPS_COUNT
            && Settings.getSetting("confirmReset")!!.loadValue(this) != "false"
        ) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.confirm_reset_title)
            builder.setMessage(R.string.confirm_reset_message)

            val click = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> r()
                    DialogInterface.BUTTON_NEGATIVE -> toast(resources.getString(R.string.confirm_reset_cancelled))
                    DialogInterface.BUTTON_NEUTRAL -> toast(resources.getString(R.string.confirm_reset_cancelled))
                }
            }

            builder.setOnCancelListener {
                toast(resources.getString(R.string.confirm_reset_cancelled))
            }

            builder.setPositiveButton(R.string.button_yes, click)
            builder.setNegativeButton(R.string.button_no, click)

            builder.create().show()
        } else {
            r()
        }
    }

    private fun undo(uniqueIdentifier: String = "", toast: Boolean = true) {
        val undo: String = currency.undo(uniqueIdentifier)

        if (toast) {
            if (undo != "") {
                toast(resources.getString(R.string.undo_success) + " " + undo)
            } else {
                toast(resources.getString(R.string.undo_fail))
            }
        }

        calc()
        currency.write(this)
    }

    private fun inc(uniqueIdentifier: String, add: Boolean = true, opList: Boolean = true) {
        currency.increment(uniqueIdentifier, add, opList)
        calc()
        currency.write(this)
    }

    private fun load() {
        //load active currency
        currency = CurrencyList().getCurrency(Settings.getSetting("activeCurrency")!!.loadValue(this))
        currency.load(this)

        //load mode (not currently in use)
        mode = Settings.getSetting("mode")!!.loadValue(this)

        //disable undo with advanced mode
        if (mode == Settings.Modes.ADVANCED.value) {
            buttonUndo.isEnabled = false
        }

        //run calc to set the sum label to 0.00
        calc()
    }

    private fun constructUI() {
        val MAX_BUTTONS_PER_ROW = 4

        val uiMap: Map<Array<MoneyPiece>, LinearLayout> = mapOf(
            currency.paperCommon to layoutPaperCommonButtons,
            currency.paperUncommon to layoutPaperUncommonButtons,
            currency.coins to layoutCoinsButtons
        )

        for ((c, l) in uiMap) {
            var currentLayout = LinearLayout(this)
            fun newRow() {
                currentLayout = LinearLayout(this)
                currentLayout.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                currentLayout.layoutParams.height =
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, 0.5f, resources.displayMetrics).toInt()
                currentLayout.orientation = LinearLayout.HORIZONTAL
                currentLayout.gravity = Gravity.CENTER_HORIZONTAL

                l.addView(currentLayout)
            }

            if (mode == Settings.Modes.BASIC.value) {
                newRow()

                for (i in c.indices) {
                    if (i % MAX_BUTTONS_PER_ROW == 0 && i > 0) newRow()

                    addEntry(c[i], currentLayout)
                }
            } else if (mode == Settings.Modes.ADVANCED.value) {
                for (i in c.indices) {
                    newRow()

                    addEntry(c[i], currentLayout)
                }
            }
        }
    }

    private fun addEntry(m: MoneyPiece, l: LinearLayout) {
        if (mode == Settings.Modes.BASIC.value) {
            val b = Button(this)
            b.text = m.display
            b.transformationMethod = null

            val layoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            layoutParams.weight = 1f
            b.layoutParams = layoutParams
            b.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics)

            b.setOnClickListener {
                inc(m.uniqueIdentifier)
            }

            l.addView(b)
        } else if (mode == Settings.Modes.ADVANCED.value) {
            val bPlus = Button(this)
            val bMinus = Button(this)
            val edit = EditText(this)
            val label = TextView(this)

            fun advButton() {
                edit.clearFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(edit.windowToken, 0)
            }

            //label
            run {
                val layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                layoutParams.weight = 1f
                label.layoutParams = layoutParams

                label.text = m.display
                label.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics)
                label.gravity = Gravity.CENTER
            }

            //text edit
            run {
                val layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                layoutParams.weight = 1f
                edit.layoutParams = layoutParams
                edit.inputType = InputType.TYPE_CLASS_NUMBER
                edit.gravity = Gravity.CENTER
                edit.setSelectAllOnFocus(true)
                edit.tag = m.uniqueIdentifier
                setEditTextText(edit, m.count.toString())

                edit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if (!edit.tag.toString().contains("_edit")) {
                            if (s != null) {
                                var n = 0

                                try {
                                    n = s.toString().toInt()
                                    n = if (n < 0) 0 else n

                                    currency.set(m.uniqueIdentifier, n)
                                } catch (e: Exception) {
                                    currency.set(m.uniqueIdentifier, 0)
                                }
                            } else {
                                currency.set(m.uniqueIdentifier, 0)
                            }

                            calc()
                            currency.write(this@CounterActivity)
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })

                advancedModeEditText.add(edit)
            }

            //button plus
            run {
                val layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                layoutParams.weight = 1f
                bPlus.layoutParams = layoutParams

                bPlus.text = resources.getString(R.string.advanced_plus)
                bPlus.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, resources.displayMetrics)

                bPlus.setOnClickListener {
                    advButton()

                    inc(m.uniqueIdentifier, add = true, opList = false)
                }
            }

            //button minus
            run {
                val layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                layoutParams.weight = 1f
                bMinus.layoutParams = layoutParams

                bMinus.text = resources.getString(R.string.advanced_minus)
                bMinus.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, resources.displayMetrics)

                bMinus.setOnClickListener {
                    advButton()

                    inc(m.uniqueIdentifier, add = false, opList = false)
                }
            }

            //add to layout
            l.addView(label)
            l.addView(bMinus)
            l.addView(edit)
            l.addView(bPlus)
        }
    }

    private fun setEditTextText(e: EditText, text: String) {
        e.tag = e.tag.toString() + "_edit"
        e.setText(text)
        e.tag = e.tag.toString().substring(0, e.tag.toString().indexOf("_edit"))
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
