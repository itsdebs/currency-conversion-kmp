package com.debanjan.currencyconversion.kmm.shared.cache

internal interface Database {
    fun clearDatabase()
    fun getAllExchangeRates(): Map<String, Double>
    fun getAllExchangeCurrenciesNames(): List<String>
    fun insertExchangeRates(exchangeRates: Map<String, Double>)
}