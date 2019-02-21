package com.degstu.quickmoneycounter.settings

class Settings {
    companion object {
        val list: Array<Setting> = arrayOf(
            Setting("activeCurrency", "USD")
        )

        fun getSetting(name: String): Setting? {
            for (s: Setting in list) {
                if (s.uniqueIdentifier == name) {
                    return s
                }
            }

            return null
        }
    }
}