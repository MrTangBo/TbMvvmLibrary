package com.tb.test

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented tb_bg_search, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under tb_bg_search.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.tb.tb_bg_search", appContext.packageName)
    }
}
