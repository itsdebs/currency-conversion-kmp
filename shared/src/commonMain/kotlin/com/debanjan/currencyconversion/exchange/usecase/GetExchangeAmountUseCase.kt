package com.debanjan.currencyconversion.exchange.usecase

import com.debanjan.currencyconversion.core.coroutine.CoroutineContextProvider
import com.debanjan.currencyconversion.exchange.model.ExchangeRatesDomainModel
import com.debanjan.currencyconversion.core.usecase.BackgroundExecutingUseCase
import com.debanjan.currencyconversion.exchange.model.ExchangeValueRequestDomainModel
import com.debanjan.currencyconversion.exchange.repository.ExchangeRatesRepository

class GetExchangeAmountUseCase(coroutineContextProvider: CoroutineContextProvider, private val
exchangeRatesRepository: ExchangeRatesRepository
) :
    BackgroundExecutingUseCase<ExchangeValueRequestDomainModel, List<ExchangeRatesDomainModel>>(coroutineContextProvider) {
    //send the base currency as request
    override suspend fun executeOnBackground(request: ExchangeValueRequestDomainModel): List<ExchangeRatesDomainModel> {
        val mapOfCurrencyToRates = exchangeRatesRepository.getExchangeRates()
        val conversionRate = mapOfCurrencyToRates[request.baseCurrency] ?: 1.0
        return mapOfCurrencyToRates.filter { it.key != request.baseCurrency }.map {
            ExchangeRatesDomainModel(it.key, (it.value/conversionRate)*request.baseCurrencyAmount)
        }
    }

}