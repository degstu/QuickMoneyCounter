package com.degstu.quickmoneycounter.currency

class CurrencyList {
    val list: Array<Currency> = arrayOf(
        /*

        Currency array format:
        Currency:
            name
            master symbol
            MP array common
            MP array uncommon
            MP array coins

         */

        //US Dollar
        //<editor-fold>
        Currency(
            "USD",
            "US Dollar",
            "$",
            arrayOf(
                MoneyPiece(
                    "USDPaper1",
                    1.0,
                    "$1"
                ),
                MoneyPiece(
                    "USDPaper5",
                    5.0,
                    "$5"
                ),
                MoneyPiece(
                    "USDPaper10",
                    10.0,
                    "$10"
                ),
                MoneyPiece(
                    "USDPaper20",
                    20.0,
                    "$50"
                )
            ),
            arrayOf(
                MoneyPiece(
                    "USDPaper2",
                    2.0,
                    "$2"
                ),
                MoneyPiece(
                    "USDPaper50",
                    50.0,
                    "$50"
                ),
                MoneyPiece(
                    "USDPaper100",
                    100.0,
                    "$100"
                )
            ),
            arrayOf(
                MoneyPiece(
                    "USDCoin1",
                    0.01,
                    "1\u00A2"
                ),
                MoneyPiece(
                    "USDCoin5",
                    0.05,
                    "5\u00A2"
                ),
                MoneyPiece(
                    "USDCoin10",
                    0.1,
                    "10\u00A2"
                ),
                MoneyPiece(
                    "USDCoin25",
                    0.25,
                    "25\u00A2"
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