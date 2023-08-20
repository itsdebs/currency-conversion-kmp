package com.debanjan.currencyconversion.android.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.debanjan.currencyconversion.exchange.repository.ExchangeRatesRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ExchangeDownloadWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams), KoinComponent {
    private val exchangeRepository: ExchangeRatesRepository by inject<ExchangeRatesRepository>()
    override suspend fun doWork(): Result {
        return if (exchangeRepository.updateExchangeRates())
            Result.success()
        else Result.failure()

    }


}