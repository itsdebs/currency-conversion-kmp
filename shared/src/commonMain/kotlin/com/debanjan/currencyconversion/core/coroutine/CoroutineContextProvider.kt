package com.debanjan.currencyconversion.core.coroutine

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineContextProvider {
    val backgroundDispatcher: CoroutineDispatcher
    val foregroundDispatcher: CoroutineDispatcher
}