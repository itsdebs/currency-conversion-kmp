package com.debanjan.currencyconversion.core.di

import com.debanjan.currencyconversion.exchange.repository.ExchangeRatesRepository
import com.debanjan.currencyconversion.kmm.shared.di.platformModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.mp.KoinPlatform.getKoin

fun initKoin(customInitialization : (KoinApplication.() -> Unit)? = null ,vararg extraModules:
Module) = startKoin {
    customInitialization?.invoke(this)
    modules(commonModule +  platformModule())
    modules(extraModules.toList())
}
val exchangeRepository : ExchangeRatesRepository
    get() = getKoin().get()