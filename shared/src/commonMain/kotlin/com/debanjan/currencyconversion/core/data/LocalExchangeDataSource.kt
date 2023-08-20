package com.debanjan.currencyconversion.core.data

import com.debanjan.currencyconversion.kmm.shared.cache.Database

interface LocalExchangeDataSource {
    suspend fun getExchangeRates(): Map<String, Double>
    suspend fun getExchangeRateNames(): List<String>

    suspend fun updateExchangeRates(exchangeRates: Map<String, Double>)
}

/**
 * Accesses the sqlight database to get the exchange rates
 *
 *
 */
internal class LocalExchangeDataSourceImpl(
   private val database: Database
) : LocalExchangeDataSource {

    override suspend fun getExchangeRates(): Map<String, Double> {
        return database.getAllExchangeRates()
    }

    override suspend fun getExchangeRateNames(): List<String> {
        return database.getAllExchangeCurrenciesNames()
    }

    override suspend fun updateExchangeRates(exchangeRates: Map<String, Double>) {
        database.insertExchangeRates(exchangeRates)
    }
}