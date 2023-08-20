package com.debanjan.currencyconversion.exchange.dummy

import com.debanjan.currencyconversion.core.coroutine.CoroutineContextProvider
import com.debanjan.currencyconversion.core.usecase.BackgroundExecutingUseCase
import kotlinx.coroutines.delay
/**
 * This is a dummy use case which was used for testing the UI. This is not used in the final
 * Will be deleted in the future
 * If needed will be moved to test folder
 */
class GetAllCurrenciesDummyUseCase(coroutineContextProvider: CoroutineContextProvider) :
    BackgroundExecutingUseCase<String?, List<String>>(coroutineContextProvider) {
    //if base is sent as request, all currencies except base to be returned, if null, all will be
    // sent
    override suspend fun executeOnBackground(request: String?): List<String> {
        delay(1000)
        return dummyExchangeRates.map { it.currency }
    }
}