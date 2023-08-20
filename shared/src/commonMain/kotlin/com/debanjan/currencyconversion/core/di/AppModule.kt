package com.debanjan.currencyconversion.core.di

val commonModule =
    contextProviderModule +
    dataSourceModule +
    repositoryModule +
    useCaseModule +
    dummyUseCaseModule +
    viewModelModule