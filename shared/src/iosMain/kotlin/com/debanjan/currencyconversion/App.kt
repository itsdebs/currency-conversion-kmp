package com.debanjan.currencyconversion

import com.debanjan.currencyconversion.exchange.ui.CurrencyScreen
import moe.tlaster.precompose.PreComposeApplication

fun MainViewController() = PreComposeApplication {
    CurrencyScreen()
}