package com.peter.azure.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class AzureScheduleToolTest {

    @Test
    fun `azure schedule`() = runBlocking {
        var num = 0
        azureSchedule {
            num = 1
        }
        delay(5100)
        assertTrue(num == 1)
    }

}