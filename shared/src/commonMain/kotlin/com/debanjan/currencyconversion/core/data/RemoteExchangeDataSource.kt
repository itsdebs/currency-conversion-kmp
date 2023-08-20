package com.debanjan.currencyconversion.core.data

import com.debanjan.currencyconversion.kmm.shared.network.ExchangeApi

interface RemoteExchangeDataSource {
    @Throws(Exception::class)
    suspend fun getExchangeRates(): Map<String, Double>
}

class RemoteExchangeDataSourceImpl(private val api: ExchangeApi) : RemoteExchangeDataSource {
    override suspend fun getExchangeRates(): Map<String, Double> {
        return api.getAllLExchangeRates().rates
    }
}