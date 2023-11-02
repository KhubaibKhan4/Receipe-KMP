package org.khubaib.receipe.network.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation

object QuoteApiClient {

    private val client = HttpClient {
        install(ContentNegotiation) {

        }
    }

}