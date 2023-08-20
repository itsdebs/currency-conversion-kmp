package com.debanjan.currencyconversion.exchange.dummy

import com.debanjan.currencyconversion.exchange.repository.ExchangeRatesRepository
import kotlinx.coroutines.delay

/**
 * This is a dummy repository which was used for testing the UI. This is not used in the final
 * Will be deleted in the future
 * If needed will be moved to test folder
 */
object ExchangeRepositoryDummy : ExchangeRatesRepository {
    override suspend fun getExchangeRates(): Map<String, Double> {
        delay(1000)
        return dummyExchangeRates.associate {
            it.currency to it.rate
        }
    }

    override suspend fun getCurrencies(): List<String> {
        delay(500)
        return dummyExchangeRates.map { it.currency }
    }

    override suspend fun updateExchangeRates(): Boolean {
        return true
    }

}