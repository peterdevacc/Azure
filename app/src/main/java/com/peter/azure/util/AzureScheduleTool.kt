/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.util

import java.util.*
import kotlin.concurrent.schedule

const val LIMIT = 5000L

inline fun azureSchedule(
    crossinline action: TimerTask.() -> Unit
) = Timer().schedule(LIMIT, action)
