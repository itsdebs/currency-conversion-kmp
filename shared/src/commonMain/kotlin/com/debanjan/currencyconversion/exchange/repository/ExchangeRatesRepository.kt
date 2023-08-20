package com.debanjan.currencyconversion.exchange.repository

import com.debanjan.currencyconversion.core.data.LocalExchangeDataSource
import com.debanjan.currencyconversion.core.data.RemoteExchangeDataSource
import com.debanjan.currencyconversion.exchange.exception.ExchangeRateFetchError
import kotlin.coroutines.cancellation.CancellationException

interface ExchangeRatesRepository {
    @Throws(ExchangeRateFetchError::class, CancellationException::class)
    suspend fun getExchangeRates(): Map<String, Double>

    @Throws(ExchangeRateFetchError::class, CancellationException::class)
    suspend fun getCurrencies(): List<String>

    suspend fun updateExchangeRates(): Boolean
}

class ExchangeRatesRepositoryImpl(
    private val localDataSource: LocalExchangeDataSource,
    private val remoteDataSource: RemoteExchangeDataSource
) : ExchangeRatesRepository {

    override suspend fun getExchangeRates(): Map<String, Double> {
        getLocalExchangeRatesIfAvailable().ifEmpty {
            updateExchangeRates()
        }
        return getLocalExchangeRatesIfAvailable().takeIf { it.isNotEmpty() } ?: throw ExchangeRateFetchError()
    }

    private suspend fun getLocalExchangeRatesIfAvailable(): Map<String, Double> {
        return try {
            return localDataSource.getExchangeRates()
        } catch (e: Exception) {
            mapOf()
        }
    }

    override suspend fun getCurrencies(): List<String> {
        getLocalExchangeCurrencyNamesIfAvailable().ifEmpty {
            updateExchangeRates()
        }
        return getLocalExchangeCurrencyNamesIfAvailable().takeIf { it.isNotEmpty() } ?: throw
        ExchangeRateFetchError()
    }

    private suspend fun getLocalExchangeCurrencyNamesIfAvailable(): List<String> {
        return try {
            return localDataSource.getExchangeRateNames()
        } catch (e: Exception) {
            listOf()
        }
    }

    override suspend fun updateExchangeRates(): Boolean {
        return try {
            val newExchangeRates = remoteDataSource.getExchangeRates()
            localDataSource.updateExchangeRates(newExchangeRates)
            true
        } catch (e: Exception) {
            println("Network error: ${e.message}")
            false
        }
    }
}

