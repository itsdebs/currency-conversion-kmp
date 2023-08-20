package com.debanjan.currencyconversion.utils

import com.debanjan.currencyconversion.core.utils.Throttler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue

class ThrottlerTest {

    @Test
    fun `test throttling`() {
        val waitFor = 1000L
        val throttler = Throttler()
        var counter = 0
        runBlocking {
            launch(Dispatchers.IO) {
                with(throttler) {
                    (1..8).forEach { _ ->
                        throttle(waitFor) {
                            counter++;
                        }
                        delay(100)
                    }
                }
            }
        }
        assertTrue(counter == 1, "Counter should be 1")

    }
}