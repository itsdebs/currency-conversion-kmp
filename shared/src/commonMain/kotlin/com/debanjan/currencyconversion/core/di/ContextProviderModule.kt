package com.debanjan.currencyconversion.core.di

import com.debanjan.currencyconversion.core.coroutine.CoroutineContextProvider
import kotlinx.coroutines.IO
import org.koin.dsl.bind
import org.koin.dsl.module

val contextProviderModule = module {
    factory {
        object: CoroutineContextProvider {
            override val backgroundDispatcher = kotlinx.coroutines.Dispatchers.Main
            override val foregroundDispatcher = kotlinx.coroutines.Dispatchers.IO
        }
    } bind CoroutineContextProvider::class
}