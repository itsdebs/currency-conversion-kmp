package com.debanjan.currencyconversion.utils

import com.debanjan.currencyconversion.kmm.shared.utils.format
import kotlin.test.Test
import kotlin.test.assertTrue

class UtilsTest {

    @Test
    fun `test double formatting`(){
        assertTrue(2.00203030303.format(2)== "2.00")
        assertTrue(2.00903030303.format(2)== "2.01")
        assertTrue(2.01903030303.format(2)== "2.02")
    }
}