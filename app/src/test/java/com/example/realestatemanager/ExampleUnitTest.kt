package com.example.realestatemanager

import com.example.realestatemanager.utils.utils
import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val nowDate = SimpleDateFormat("dd/MM/yyyy").format(Date())

    @Test
    fun convertDollarToEuro() {
        assertEquals(16240, utils.convertDollarToEuro(20000))
    }

    @Test
    fun convertEuroToDollar() {
        assertEquals(24000, utils.convertEuroToDollar(20000))
    }

    @Test
    fun getTodayDate() {
        assertEquals(nowDate, utils.getTodayDate())
    }
}
