package com.debanjan.currencyconversion.exchange.dummy

import com.debanjan.currencyconversion.core.coroutine.CoroutineContextProvider
import com.debanjan.currencyconversion.core.usecase.BackgroundExecutingUseCase
import com.debanjan.currencyconversion.exchange.model.ExchangeRatesDomainModel
import com.debanjan.currencyconversion.exchange.model.ExchangeValueRequestDomainModel
import kotlinx.coroutines.delay
/**
 * This is a dummy use case which was used for testing the UI. This is not used in the final
 * Will be deleted in the future
 * If needed will be moved to test folder
 */
class GetExchangeRatesDummyUseCase(coroutineContextProvider: CoroutineContextProvider) :
    BackgroundExecutingUseCase<ExchangeValueRequestDomainModel, List<ExchangeRatesDomainModel>>(coroutineContextProvider) {
    //send the base currency as request
    override suspend fun executeOnBackground(request: ExchangeValueRequestDomainModel): List<ExchangeRatesDomainModel> {
        delay(1000)
        val mapOfCurrencyToRates = dummyExchangeRates.associate { it.currency to it.rate }// to
        // keep aligned with the repository
        val conversionRate = mapOfCurrencyToRates[request.baseCurrency] ?: 1.0
        return mapOfCurrencyToRates.filter { it.key != request.baseCurrency }.map {
            ExchangeRatesDomainModel(it.key, (it.value/conversionRate)*request.baseCurrencyAmount)
        }

    }

}