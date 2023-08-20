package com.debanjan.currencyconversion.exchange.model

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRatesDomainModel(val currency: String, val rate: Double)
