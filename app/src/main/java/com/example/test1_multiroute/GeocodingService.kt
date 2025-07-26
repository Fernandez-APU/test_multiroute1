package com.example.test1_multiroute

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.engine.cio.*
import kotlinx.serialization.json.*

object GeocodingService {
    private const val apiKey = "YOUR_TOMTOM_API_KEY"

    suspend fun getLatLong(address: String): Pair<Double, Double>? {
        val client = HttpClient(CIO)
        val url = "https://api.tomtom.com/search/2/geocode/${address}.json?key=$apiKey"

        val response: String = client.get(url)
        client.close()

        val json = Json.parseToJsonElement(response).jsonObject
        val position = json["results"]?.jsonArray?.get(0)?.jsonObject
            ?.get("position")?.jsonObject ?: return null

        val lat = position["lat"]?.jsonPrimitive?.double ?: return null
        val lon = position["lon"]?.jsonPrimitive?.double ?: return null

        return Pair(lat, lon)
    }
}