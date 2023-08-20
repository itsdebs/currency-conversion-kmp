package com.debanjan.currencyconversion.core.di

import com.debanjan.currencyconversion.core.data.RemoteExchangeDataSource
import com.debanjan.currencyconversion.core.data.LocalExchangeDataSource
import com.debanjan.currencyconversion.core.data.LocalExchangeDataSourceImpl
import com.debanjan.currencyconversion.core.data.RemoteExchangeDataSourceImpl
import com.debanjan.currencyconversion.kmm.shared.cache.Database
import com.debanjan.currencyconversion.kmm.shared.cache.DatabaseImpl
import com.debanjan.currencyconversion.kmm.shared.network.ExchangeApi
import com.debanjan.currencyconversion.kmm.shared.network.ExchangeApiImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val dataSourceModule = module{
    single {
        DatabaseImpl(get())
    } bind Database::class
    single() {
        LocalExchangeDataSourceImpl(get())
    } bind LocalExchangeDataSource::class

    single {
        ExchangeApiImpl()
    } bind ExchangeApi::class
    single() {
        RemoteExchangeDataSourceImpl(get())
    } bind RemoteExchangeDataSource::class


}