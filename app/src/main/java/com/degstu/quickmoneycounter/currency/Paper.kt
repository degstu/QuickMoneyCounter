package com.degstu.quickmoneycounter.currency

class Paper(uniqueIdentifier: String, numericValue: Int, symbol: String, symbolFirst: Boolean = true) : MoneyPiece(
    uniqueIdentifier,
    numericValue.toDouble(),
    symbol,
    symbolFirst
)