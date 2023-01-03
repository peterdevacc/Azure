package com.peter.azure.util

import java.util.*
import kotlin.concurrent.schedule

const val LIMIT = 5000L

inline fun azureSchedule(
    crossinline action: TimerTask.() -> Unit
) = Timer().schedule(LIMIT, action)
