package com.debanjan.currencyconversion.exchange.exception

class ExchangeRateFetchError : Exception() {
    override val message: String
        get() = "Error fetching exchange rates"
}