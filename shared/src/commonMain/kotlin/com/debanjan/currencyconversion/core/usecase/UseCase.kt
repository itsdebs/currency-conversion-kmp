package com.debanjan.currencyconversion.core.usecase

interface UseCase<REQUEST,RESULT> {
    suspend fun execute(request: REQUEST): RESULT
    suspend fun execute(input: REQUEST, onResult: (RESULT) -> Unit)
}