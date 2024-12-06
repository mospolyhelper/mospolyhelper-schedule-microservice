package com.mospolytech.data.base

import kotlinx.coroutines.delay
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

suspend fun <T> retryIO(
    times: Int = 10,
    initialDelay: Long = 100, // 0.1 second
    maxDelay: Long = 1000, // 1 second
    factor: Double = 2.0,
    block: suspend () -> T,
): T {
    var currentDelay = initialDelay
    repeat(times - 1) {
        try {
            return block()
        } catch (e: IOException) {
            // you can log an error here and/or make a more finer-grained
            // analysis of the cause to see if retry is needed
        } catch (e: SerializationException) {
            // Сервер вуза может вернуть невалидный json, если
            // одновременно смотреть другие странички
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return block() // last attempt
}
