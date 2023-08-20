package com.debanjan.currencyconversion

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform