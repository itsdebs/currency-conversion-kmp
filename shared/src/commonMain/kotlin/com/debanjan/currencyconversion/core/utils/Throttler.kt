package com.debanjan.currencyconversion.core.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

//One instance to keep track of one throttle job
class Throttler {
    private var job: Job? = null

    fun CoroutineScope.throttle(
        waiFor: Long,
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job{
        job?.cancel()
        return launch(context, start) {
            delay(waiFor)
            block()
        }.also {
            job = it
        }
    }



}