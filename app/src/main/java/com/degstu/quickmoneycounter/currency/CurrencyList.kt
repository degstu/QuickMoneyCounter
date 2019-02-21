package com.degstu.quickmoneycounter.currency

class CurrencyList {
    val list: Array<Currency> = arrayOf(
        /*

        Currency array format:
        Currency:
            name
            master symbol
            paper array common
            paper array uncommon
            coins

         */

        //US Dollar
        //<editor-fold>
        Currency(
            "USD",
            "US Dollar",
            "$",
            arrayOf(
                Paper(
                    "USDPaper1",
                    1,
                    "$",
                    true
                ),
                Paper(
                    "USDPaper5",
                    5,
                    "$",
                    true
                ),
                Paper(
                    "USDPaper10",
                    10,
                    "$",
                    true
                ),
                Paper(
                    "USDPaper20",
                    20,
                    "$",
                    true
                )
            ),
            arrayOf(
                Paper(
                    "USDPaper2",
                    2,
                    "$",
                    true
                ),
                Paper(
                    "USDPaper50",
                    50,
                    "$",
                    true
                ),
                Paper(
                    "USDPaper100",
                    100,
                    "$",
                    true
                )
            ),
            arrayOf(
                Coin(
                    "USDCoin1",
                    0.01,
                    "\u00A2",
                    "Penny",
                    false
                ),
                Coin(
                    "USDCoin5",
                    0.05,
                    "\u00A2",
                    "Nickle",
                    false
                ),
                Coin(
                    "USDCoin10",
                    0.1,
                    "\u00A2",
                    "Dime",
                    false
                ),
                Coin(
                    "USDCoin25",
                    0.25,
                    "\u00A2",
                    "Quarter",
                    false
                )
            )
        )
        //</editor-fold>
    )

    fun getCurrency(name: String): Currency {
        for (c: Currency in list) {
            if (c.uniqueIdentifier == name) {
                return c
            }
        }

        return Currency("ERR", "ERR", "ERR", arrayOf(), arrayOf(), arrayOf())
    }
}