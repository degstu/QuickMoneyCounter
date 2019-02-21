package com.degstu.quickmoneycounter.currency

class Coin(uniqueIdentifier: String, numericValue: Double, symbol: String, val pieceName: String, symbolFirst: Boolean = true) : MoneyPiece(
    uniqueIdentifier,
    numericValue,
    symbol,
    symbolFirst
)