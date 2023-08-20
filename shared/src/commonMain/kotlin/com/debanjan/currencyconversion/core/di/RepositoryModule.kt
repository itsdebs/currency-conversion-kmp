package com.debanjan.currencyconversion.core.di

import com.debanjan.currencyconversion.exchange.repository.ExchangeRatesRepository
import com.debanjan.currencyconversion.exchange.repository.ExchangeRatesRepositoryImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {

    single {
        ExchangeRatesRepositoryImpl(get(), get())
    } bind ExchangeRatesRepository::class

}