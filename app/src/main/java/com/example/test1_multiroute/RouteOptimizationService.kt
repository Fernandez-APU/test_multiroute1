package com.example.test1_multiroute

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.engine.cio.*
import kotlinx.serialization.json.*

object RouteOptimizationService {

    private const val apiKey = "YOUR_TOMTOM_API_KEY"

    suspend fun optimizeRoute(waypoints: List<Pair<Double, Double>>): JsonObject? {
        val client = HttpClient(CIO)
        val url = "https://api.tomtom.com/routing/waypointoptimization/1?key=$apiKey"

        val requestBody = buildJsonObject {
            put("waypoints", JsonArray(waypoints.map { wp ->
                buildJsonObject {
                    put("point", buildJsonObject {
                        put("latitude", wp.first)
                        put("longitude", wp.second)
                    })
                }
            }))
        }

        val response: String = client.post(url) {
            header("Content-Type", "application/json")
            setBody(requestBody.toString())
        }

        client.close()
        return Json.parseToJsonElement(response).jsonObject
    }
}