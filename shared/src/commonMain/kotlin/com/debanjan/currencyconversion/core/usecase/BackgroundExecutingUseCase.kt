package com.debanjan.currencyconversion.core.usecase

import com.debanjan.currencyconversion.core.coroutine.CoroutineContextProvider
import kotlinx.coroutines.withContext

abstract class BackgroundExecutingUseCase<REQUEST, RESULT>(
    private val coroutineContextProvider: CoroutineContextProvider
) : UseCase<REQUEST, RESULT> {
    override suspend fun execute(request: REQUEST): RESULT {
        return withContext(coroutineContextProvider.backgroundDispatcher) {
            executeOnBackground(request)
        }
    }

    override suspend fun execute(input: REQUEST, onResult: (RESULT) -> Unit) {
        withContext(coroutineContextProvider.backgroundDispatcher) {
            val result = executeOnBackground(input)
            withContext(coroutineContextProvider.foregroundDispatcher) {
                onResult(result)
            }
        }
    }

    protected abstract suspend fun executeOnBackground(request: REQUEST): RESULT
}
