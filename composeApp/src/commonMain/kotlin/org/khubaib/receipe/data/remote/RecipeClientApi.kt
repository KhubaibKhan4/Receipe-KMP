package org.khubaib.receipe.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.khubaib.receipe.data.model.Recipes


object RecipeClientApi{
    @OptIn(ExperimentalSerializationApi::class)
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                //isLenient = true
                explicitNulls = true
            })
        }
        install(HttpTimeout) {
            val timeout = 30000L
            connectTimeoutMillis = timeout
            requestTimeoutMillis = timeout
            socketTimeoutMillis = timeout
        }

    }

    suspend fun getRecipes(): Recipes {
        val url =
            "https://themealdb.com/api/json/v1/1/random.php"
        return client.get(url).body()
    }

    suspend fun getSearchRecipes(query: String): Recipes {
        val url =
            "https://themealdb.com/api/json/v1/1/search.php?s=${query}"
        return client.get(url).body()
    }


}