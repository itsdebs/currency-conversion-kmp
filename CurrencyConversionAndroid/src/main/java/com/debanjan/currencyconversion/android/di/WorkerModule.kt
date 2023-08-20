package com.debanjan.currencyconversion.android.di


import com.debanjan.currencyconversion.android.work.ExchangeDownloadWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workerModule = module {
    worker {
        ExchangeDownloadWorker(get(), get())
    }
}