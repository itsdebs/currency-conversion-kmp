package com.debanjan.currencyconversion.android

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.debanjan.currencyconversion.android.di.workerModule
import com.debanjan.currencyconversion.android.work.ExchangeDownloadWorker
import com.debanjan.currencyconversion.core.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import java.util.concurrent.TimeUnit

class CurrencyConversionApplication : Application() {
    companion object{
        const val EXCHANGE_WORKER_INTERVAL = 30L //30 minutes
    }
    override fun onCreate() {
        super.onCreate()
//        startWorkManager()
        initKoin({
            androidContext(this@CurrencyConversionApplication)
            workManagerFactory()
        }, extraModules = arrayOf( workerModule))
        startWorkManager()
    }
    private val exchangeRateDownloadWorkRequest = PeriodicWorkRequestBuilder<ExchangeDownloadWorker>(CurrencyConversionApplication.EXCHANGE_WORKER_INTERVAL, TimeUnit.MINUTES)
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
        .build()


    private fun startWorkManager() {
        WorkManager.getInstance(this).enqueue(exchangeRateDownloadWorkRequest)
    }
}