package com.degstu.quickmoneycounter

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.degstu.quickmoneycounter.currency.CurrencyList
import com.degstu.quickmoneycounter.settings.Settings
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setResult(Activity.RESULT_OK)

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

        run {
            val modeMap: Array<String> = arrayOf(
                Settings.Modes.BASIC.value,
                Settings.Modes.ADVANCED.value
            )
            spinnerMode.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, modeMap)
            spinnerMode.setSelection(0)
            val settingsVal: String = Settings.getSetting("mode")!!.loadValue(this)
            for (i in modeMap.indices) if (modeMap[i] == settingsVal) spinnerMode.setSelection(i)
            spinnerMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    spinnerMode.setSelection(0)
                    Settings.getSetting("mode")!!.writeValue(
                        this@SettingsActivity,
                        Settings.Modes.BASIC.value
                    )
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Settings.getSetting("mode")!!.writeValue(
                        this@SettingsActivity,
                        modeMap[position]
                    )
                }
            }
        }

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
