package org.khubaib.receipe.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.khubaib.receipe.data.model.RecipeX
import org.khubaib.receipe.data.model.Recipes
import org.khubaib.receipe.util.network.AppConstant


@OptIn(ExperimentalSerializationApi::class)
val client = HttpClient {
    defaultRequest {
        url {
            takeFrom(AppConstant.BASE_URL)
            parameters.append("api_key", AppConstant.API_KEY)
        }
    }
    expectSuccess = true

    install(HttpTimeout) {
        val timeout = 30000L
        connectTimeoutMillis = timeout
        requestTimeoutMillis = timeout
        socketTimeoutMillis = timeout
    }

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
            explicitNulls = true
        })
    }
}

suspend fun getRecipes(): Recipes {
    val url =
        "https://api.spoonacular.com/recipes/random?apiKey=117c211f113f4b9aa141f3d69fa460b1"
    return client.get(url).body()
}

suspend fun getSearchRecipes(query: String): Recipes {
    val url =
        "https://api.spoonacular.com/recipes/complexSearch?apiKey=117c211f113f4b9aa141f3d69fa460b1&query=${query}"
    return client.get(url).body()
}

