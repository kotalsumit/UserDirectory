package com.sumit.userdirectory.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {
    fun create(): HttpClient {
        return HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(networkJson)
            }
        }
    }

    val networkJson: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }
}
