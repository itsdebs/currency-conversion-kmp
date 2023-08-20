package com.debanjan.currencyconversion

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}
@Composable
fun HelloWorld() {
    Text("Hello World!")
}