package com.debanjan.currencyconversion.core.di

import com.debanjan.currencyconversion.exchange.ui.ExchangeViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    factory {
        ExchangeViewModel( get(named("getCurrencies")), get(named("getExchangeRates")))
    }
}