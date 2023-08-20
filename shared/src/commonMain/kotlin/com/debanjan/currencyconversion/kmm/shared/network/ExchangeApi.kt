package com.debanjan.currencyconversion.kmm.shared.network

import com.debanjan.currencyconversion.core.data.ExchangeApiModel
import io.ktor.client.HttpClient

interface ExchangeApi {
    val httpClient: HttpClient

    @Throws(Exception::class)
    suspend fun getAllLExchangeRates(): ExchangeApiModel
}