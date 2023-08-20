package com.debanjan.currencyconversion.core.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeApiModel(@SerialName("timestamp") val timestamp: Long,
                            @SerialName("base") val base: String,
                            @SerialName("rates") val rates: Map<String, Double>)
