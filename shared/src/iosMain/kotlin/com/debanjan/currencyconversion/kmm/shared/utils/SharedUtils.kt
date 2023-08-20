package com.debanjan.currencyconversion.kmm.shared.utils
import platform.Foundation.stringWithFormat

actual fun Double.format(digits: Int) : String {
    return platform.Foundation.NSString.stringWithFormat("%.2f", this)
}