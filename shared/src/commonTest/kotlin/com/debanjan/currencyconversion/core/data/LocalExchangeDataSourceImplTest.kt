package com.debanjan.currencyconversion.core.data

import com.debanjan.currencyconversion.kmm.shared.cache.Database
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalExchangeDataSourceImplTest {

    @Test
    fun `getExchangeRates returns data from the database`() = runBlocking {
        val exchangeRates = mapOf("USD" to 1.0, "EUR" to 0.85)
        val database = mockDatabase(exchangeRates = exchangeRates)
        val dataSource = LocalExchangeDataSourceImpl(database)

        val result = dataSource.getExchangeRates()

        assertEquals(exchangeRates, result)
    }

    @Test
    fun `getExchangeRateNames returns currency names from the database`() = runBlocking {
        val currencies = mapOf("USD" to 1.0, "EUR" to 1.5, "JPY" to 1000.0)
        val database = mockDatabase(exchangeRates = currencies)
        val dataSource = LocalExchangeDataSourceImpl(database)

        val result = dataSource.getExchangeRateNames()

        assertEquals(currencies.keys.toList(), result)
    }

    @Test
    fun `updateExchangeRates inserts data into the database`() = runBlocking {
        val exchangeRates = mapOf("USD" to 1.1, "EUR" to 0.9)
        val database = mockDatabase()
        val dataSource = LocalExchangeDataSourceImpl(database)

        dataSource.updateExchangeRates(exchangeRates)

        assertEquals(exchangeRates, database.getAllExchangeRates())
    }

    private fun mockDatabase(
        exchangeRates: Map<String, Double> = emptyMap()): Database {
        return object : Database {
            private var exchangeRatesMap = exchangeRates
            override fun clearDatabase() {
                // Not needed for testing
            }

            override fun getAllExchangeRates(): Map<String, Double> = exchangeRatesMap

            override fun getAllExchangeCurrenciesNames(): List<String> = exchangeRatesMap.keys.toList()

            override fun insertExchangeRates(exchangeRates: Map<String, Double>) {
                this.exchangeRatesMap = exchangeRates
            }
        }
    }
}
