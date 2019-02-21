package com.degstu.quickmoneycounter.currency

class Currency(
    val uniqueIdentifier: String,
    val name: String,
    val masterSymbol: String,
    val paperCommon: Array<Paper>,
    val paperUncommon: Array<Paper>,
    val coins: Array<Coin>
)