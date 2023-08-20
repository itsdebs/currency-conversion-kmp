package com.debanjan.currencyconversion.kmm.shared.network

import com.debanjan.currencyconversion.core.data.ExchangeApiModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ExchangeApiImpl : ExchangeApi {
    companion object{
        const val EXCHANGE_URL = "https://openexchangerates.org/api/latest.json?app_id=5ceda06c56c84a0a80b062b287777692"
    }
    override val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }
    @Throws(Exception::class)
    override suspend fun getAllLExchangeRates(): ExchangeApiModel {
        return httpClient.get(EXCHANGE_URL)
            .body()
    }
}