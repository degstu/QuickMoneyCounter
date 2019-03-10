package com.degstu.quickmoneycounter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.degstu.quickmoneycounter.settings.Settings
import kotlinx.android.synthetic.main.activity_eula.*

class EulaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eula)

        cbEulaAgree.setOnClickListener {
            if (cbEulaAgree.isChecked) {
                Settings.getSetting("eula")!!.writeValue(this, "true")
                finish()
            }
        }

        buttonDecline.setOnClickListener {
            finish()
        }
    }
}
