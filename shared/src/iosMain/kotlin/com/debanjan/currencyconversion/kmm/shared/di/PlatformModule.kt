package com.debanjan.currencyconversion.kmm.shared.di

import com.debanjan.currencyconversion.kmm.shared.cache.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { DatabaseDriverFactory() }
}