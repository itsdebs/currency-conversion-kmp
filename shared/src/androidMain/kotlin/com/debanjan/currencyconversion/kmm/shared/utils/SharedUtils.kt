package com.debanjan.currencyconversion.kmm.shared.utils

actual fun Double.format(digits: Int) : String {
    return "%.${digits}f".format(this)
}