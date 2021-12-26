package renetik.java.util.concurrent

import renetik.android.framework.common.catchAllError
import renetik.android.framework.util.CSMainHandler.postOnMain
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.NANOSECONDS

fun ScheduledExecutorService.background(delay: Long = 0, function: () -> Unit) =
    schedule({ catchAllError { function() } }, delay, MILLISECONDS)

fun ScheduledExecutorService.looperBackgroundNano(delay: Long = 0, function: () -> Unit) =
    schedule({ catchAllError { function() } }, delay, NANOSECONDS)

fun ScheduledExecutorService.backgroundRunOnUi(delay: Long = 0, function: () -> Unit) =
    background(delay) { postOnMain(function) }

fun ScheduledExecutorService.backgroundRepeat(delay: Long = 0,
                                              period: Long, function: () -> Unit) =
    scheduleAtFixedRate({ catchAllError { function() } }, delay, period, MILLISECONDS)

fun ScheduledExecutorService.backgroundRepeatNano(delay: Long = 0,
                                              period: Long, function: () -> Unit) =
    scheduleAtFixedRate({ catchAllError { function() } }, delay, period, NANOSECONDS)

fun ScheduledExecutorService.backgroundRepeatRunOnUI(delay: Long = 0,
                                                     period: Long, function: () -> Unit) =
    backgroundRepeat(delay, period) { postOnMain(function) }

fun ScheduledExecutorService.backgroundRepeat(period: Long, function: () -> Unit) =
    backgroundRepeat(delay = period, period = period, function = function)

fun ScheduledExecutorService.backgroundRepeatNano(period: Long, function: () -> Unit) =
    backgroundRepeatNano(delay = period, period = period, function = function)