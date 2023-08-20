package com.debanjan.currencyconversion.core.di

import com.debanjan.currencyconversion.core.usecase.UseCase
import com.debanjan.currencyconversion.exchange.dummy.GetAllCurrenciesDummyUseCase
import com.debanjan.currencyconversion.exchange.dummy.GetExchangeRatesDummyUseCase
import com.debanjan.currencyconversion.exchange.usecase.GetAllCurrenciesUseCase
import com.debanjan.currencyconversion.exchange.usecase.GetExchangeAmountUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val useCaseModule = module{

    factory(named("getExchangeRates")) {
        GetExchangeAmountUseCase(get(), get())
    } bind UseCase::class
    factory(named("getCurrencies")) {
        GetAllCurrenciesUseCase(get(), get())
    } bind UseCase::class


}
//dummy use case module for testing
//will be removed in future
val dummyUseCaseModule = module {
    factory(named("getExchangeRates_dummy")) {
        GetExchangeRatesDummyUseCase(get())
    } bind UseCase::class
    factory(named("getCurrencies_dummy")) {
        GetAllCurrenciesDummyUseCase(get())
    } bind UseCase::class

}