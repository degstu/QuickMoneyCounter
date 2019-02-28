package com.degstu.quickmoneycounter.settings

class Settings {
    enum class Modes(val value: String) {
        BASIC("Basic"),
        ADVANCED("Advanced")
    }

    companion object {
        val list: Array<Setting> = arrayOf(
            Setting("activeCurrency", "USD"),
            Setting("mode", Settings.Modes.BASIC.value),
            Setting("confirmReset", "true")
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