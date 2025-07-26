package com.example.test1_multiroute

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlinx.serialization.json.*

fun main() {
    embeddedServer(Netty, port = 8080) {
        routing {
            post("/getOptimizedRoute") {
                val waypoints = call.receive<List<Pair<Double, Double>>>()

                val optimizedRoute = RouteOptimizationService.optimizeRoute(waypoints)

                call.respond(optimizedRoute ?: JsonObject(emptyMap()))
            }
        }
    }.start(wait = true)
}