package com.degstu.quickmoneycounter

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import com.degstu.quickmoneycounter.currency.CurrencyList
import com.degstu.quickmoneycounter.settings.Settings
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setResult(Activity.RESULT_OK)

        //currency spinner
        run {
            val clist: CurrencyList = CurrencyList()
            spinnerCurrency.adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                clist.list.map { it.name }.toTypedArray()
            )
            spinnerCurrency.setSelection(0)
            for (i in clist.list.indices)
                if (clist.list[i].uniqueIdentifier == Settings.getSetting("activeCurrency")!!.loadValue(this))
                    spinnerCurrency.setSelection(i)
            spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    spinnerCurrency.setSelection(0)
                    Settings.getSetting("activeCurrency")!!.writeValue(
                        this@SettingsActivity,
                        clist.list[0].uniqueIdentifier
                    )
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Settings.getSetting("activeCurrency")!!.writeValue(
                        this@SettingsActivity,
                        clist.list[position].uniqueIdentifier
                    )
                }
            }
        }

        //mode
        run {
            val modeMap: Array<String> = arrayOf(
                Settings.Modes.BASIC.value,
                Settings.Modes.ADVANCED.value
            )

            for (mode in modeMap) {
                val rb = RadioButton(this)
                rb.text = mode
                rb.setOnClickListener {
                    Settings.getSetting("mode")!!.writeValue(this, mode)
                }

                rgMode.addView(rb)
            }

            rgMode.check(rgMode.getChildAt(1).id)

            for (i in 0 until rgMode.childCount) {
                if ((rgMode.getChildAt(i) as RadioButton).text == Settings.getSetting("mode")!!.loadValue(this))
                    rgMode.check(rgMode.getChildAt(i).id)
            }
        }

        //reset confirm
        run {
            checkBoxConfirmReset.isChecked = Settings.getSetting("confirmReset")!!.loadValue(this) != "false"
            checkBoxConfirmReset.setOnClickListener {
                Settings.getSetting("confirmReset")!!.writeValue(
                    this,
                    if (checkBoxConfirmReset.isChecked) "true" else "false"
                )
            }
        }
    }
}
