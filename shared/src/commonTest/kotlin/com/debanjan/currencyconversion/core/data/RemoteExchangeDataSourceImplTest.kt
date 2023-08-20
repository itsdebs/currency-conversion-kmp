package com.debanjan.currencyconversion.core.data

import com.debanjan.currencyconversion.kmm.shared.network.ExchangeApi
import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RemoteExchangeDataSourceImplTest {

    @Test
    fun `getExchangeRates returns rates from the API`() = runBlocking {
        val exchangeRates = mapOf("USD" to 1.1, "EUR" to 0.9)
        val api = mockExchangeApi(exchangeRates = exchangeRates)
        val dataSource = RemoteExchangeDataSourceImpl(api)

        val result = dataSource.getExchangeRates()

        assertEquals(exchangeRates, result)
    }

    @Test
    fun `getExchangeRates throws exception if API call fails`() {
        runBlocking {
            val api = mockFailingExchangeApi()
            val dataSource = RemoteExchangeDataSourceImpl(api)

            assertFailsWith<Exception> {
                dataSource.getExchangeRates()
            }
        }
    }

    private fun mockExchangeApi(exchangeRates: Map<String, Double>): ExchangeApi {
        return object : ExchangeApi {
            override val httpClient: HttpClient = mockHttpClient()

            override suspend fun getAllLExchangeRates(): ExchangeApiModel {
                return ExchangeApiModel(base = "USD", rates = exchangeRates, timestamp = Clock
                    .System.now().toEpochMilliseconds())
            }
        }
    }

    private fun mockFailingExchangeApi(): ExchangeApi {
        return object : ExchangeApi {
            override val httpClient: HttpClient = mockHttpClient()

            override suspend fun getAllLExchangeRates(): ExchangeApiModel {
                throw Exception("API call failed")
            }
        }
    }

    private fun mockHttpClient() =
        HttpClient()

}
