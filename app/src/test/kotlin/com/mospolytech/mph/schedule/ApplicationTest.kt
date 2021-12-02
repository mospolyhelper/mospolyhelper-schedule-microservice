package com.mospolytech.mph.schedule

import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.locations.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.server.sessions.*
import io.ktor.server.plugins.*
import io.ktor.serialization.kotlinx.json.*
import io.micrometer.prometheus.*
import io.ktor.server.metrics.micrometer.*
import org.slf4j.event.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.mospolytech.mph.schedule.plugins.*

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ configureRouting() }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Hello World!", response.content)
            }
        }
    }
}