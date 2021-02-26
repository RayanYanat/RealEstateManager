package com.example.realestatemanager

import androidx.test.platform.app.InstrumentationRegistry
import com.example.realestatemanager.utils.utils
import junit.framework.Assert.assertTrue
import org.junit.Test

class InternetTest {

    @Test
    @Throws(Exception::class)
    fun isInternetAvailable() {
        assertTrue(utils.isInternetAvailable(InstrumentationRegistry.getInstrumentation()
            .context))
    }
}