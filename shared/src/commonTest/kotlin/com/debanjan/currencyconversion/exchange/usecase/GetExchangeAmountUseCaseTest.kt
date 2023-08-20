package com.debanjan.currencyconversion.exchange.usecase

import com.debanjan.currencyconversion.core.coroutine.CoroutineContextProvider
import com.debanjan.currencyconversion.exchange.dummy.ExchangeRepositoryDummy
import com.debanjan.currencyconversion.exchange.model.ExchangeValueRequestDomainModel
import com.debanjan.currencyconversion.exchange.repository.ExchangeRatesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

/**
 * Write full coverage test cases for GetExchangeAmountUseCase
 *
 */
class GetExchangeAmountUseCaseTest {
    private val contextProvider = object : CoroutineContextProvider {
        override val backgroundDispatcher = Dispatchers.Unconfined
        override val foregroundDispatcher = Dispatchers.IO
    }
    /**
     * Function to check if repository gets the correct base currency
     */
    @Test
    fun `test repository being called with valid params`() {
        var counter = 0

        val repository = object : ExchangeRatesRepository {
            override suspend fun getExchangeRates(): Map<String, Double> {
                delay(300)
                ++ counter
                return mapOf()
            }

            override suspend fun getCurrencies(): List<String> {
                return listOf()
            }

            override suspend fun updateExchangeRates(): Boolean {
                return true
            }
        }
        val useCase = GetExchangeAmountUseCase(contextProvider, repository)
        runBlocking {
            useCase.execute(ExchangeValueRequestDomainModel("EUR", 1.0))
            assertEquals(1, counter)
        }
      }
    @Test
    fun `test correct value is returned`() {
        val useCase = GetExchangeAmountUseCase(contextProvider, ExchangeRepositoryDummy)
        runBlocking {
            val expectedData = ExchangeRepositoryDummy.getExchangeRates()
            val testBase = expectedData.keys.first()
            val testAmount = 10.0
            val exchangeRates = useCase.execute(ExchangeValueRequestDomainModel(testBase, testAmount))
            assertEquals(expectedData.size - 1, exchangeRates.size)
            exchangeRates.forEach {
                assertEquals(it.rate, ((expectedData[it.currency]?:1.0)/(expectedData[testBase]?:1.0))
                    .times
                    (testAmount))
            }
        }

    }

    @Test
    fun `test error returned by repository is propagated`() {
        val useCase = GetExchangeAmountUseCase(contextProvider, object : ExchangeRatesRepository {
            override suspend fun getExchangeRates(): Map<String, Double> {
                throw Exception()
            }

            override suspend fun getCurrencies(): List<String> {
                throw Exception()
            }

            override suspend fun updateExchangeRates(): Boolean {
                throw Exception()
            }
        })
        runBlocking {
            try {
                useCase.execute(ExchangeValueRequestDomainModel("EUR", 1.0))
                fail("Should have thrown exception")
            } catch (e: Exception) {
                // Expected
            }
        }
    }
}

