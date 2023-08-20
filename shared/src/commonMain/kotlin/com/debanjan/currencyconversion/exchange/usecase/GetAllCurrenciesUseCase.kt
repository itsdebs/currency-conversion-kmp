package com.debanjan.currencyconversion.exchange.usecase

import com.debanjan.currencyconversion.core.coroutine.CoroutineContextProvider
import com.debanjan.currencyconversion.core.usecase.BackgroundExecutingUseCase
import com.debanjan.currencyconversion.exchange.repository.ExchangeRatesRepository

class GetAllCurrenciesUseCase(coroutineContextProvider: CoroutineContextProvider,
    private val exchangeRatesRepository: ExchangeRatesRepository) :
    BackgroundExecutingUseCase<String?, List<String>>(coroutineContextProvider) {
    /**
     * 1. Fetch the list of currencies from the repository
     * 2. If the request is null, return the list as it is
     * 3. If the request is not null, remove the request from the list and return the list
     *
     * @param request base currency
     * @return list of currencies except the base currency
     */
    override suspend fun executeOnBackground(request: String?): List<String> {
        val currencies = exchangeRatesRepository.getCurrencies()
        return if (request == null) {
            currencies
        } else {
            currencies.filter { it != request }
        }
    }
}