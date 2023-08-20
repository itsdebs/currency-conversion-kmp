package com.debanjan.currencyconversion.exchange.usecase

import com.debanjan.currencyconversion.core.coroutine.CoroutineContextProvider
import com.debanjan.currencyconversion.exchange.dummy.ExchangeRepositoryDummy
import com.debanjan.currencyconversion.exchange.repository.ExchangeRatesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class GetAllCurrenciesUseCaseTest {
    private val contextProvider = object : CoroutineContextProvider {
        override val backgroundDispatcher = Dispatchers.Unconfined
        override val foregroundDispatcher = Dispatchers.IO
    }


    @Test
    fun `test repository is called with valid Arguments`() {
        val testBase = "EUR"
        var repositoryFetchCounter = 0
        val getAllCurrenciesUseCase =
            GetAllCurrenciesUseCase(contextProvider, object : ExchangeRatesRepository {
                override suspend fun getExchangeRates(): Map<String, Double> {
                    return mapOf()
                }

                override suspend fun getCurrencies(): List<String> {
                    delay(500)
                    repositoryFetchCounter ++
                    return listOf()
                }

                override suspend fun updateExchangeRates(): Boolean {
                    // NO OP
                    return true
                }
            })
        runBlocking {
            getAllCurrenciesUseCase.execute(testBase)
        }
        assertEquals(1, repositoryFetchCounter)
    }
    @Test
    fun `test get all currencies`() {
        runBlocking {
            val expectedData = ExchangeRepositoryDummy.getCurrencies()
            val testBase = expectedData[0]
            val getAllCurrenciesUseCase =
                GetAllCurrenciesUseCase(contextProvider, ExchangeRepositoryDummy)
            val currencies = getAllCurrenciesUseCase.execute(testBase)
            assertEquals(expectedData.size - 1, currencies.size)
        }
    }

    @Test
    fun `test error is thrown when repository throws error`() {
        val getAllCurrenciesUseCase =
            GetAllCurrenciesUseCase(contextProvider, object : ExchangeRatesRepository {
                override suspend fun getExchangeRates(): Map<String, Double> {
                    return mapOf()
                }

                override suspend fun getCurrencies(): List<String> {
                    throw Exception()
                }

                override suspend fun updateExchangeRates(): Boolean {
                    // NO OP
                    return true
                }
            })
        runBlocking {
            try {
                getAllCurrenciesUseCase.execute("EUR")
                fail("Expected Exception")
            } catch (e: Exception) {
                // Expected
            }
        }
    }
}