package org.khubaib.receipe.util.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.json.Json
import org.khubaib.receipe.data.model.Recipes

class ApiImp(private val httpClient: HttpClient) : ApiInterface {
    override suspend fun recipeRandom(): Recipes {
        val response: HttpResponse = httpClient.get("https://api.spoonacular.com/recipes/random?apiKey=7423fe4fac8f428cbb46be6643272223")
        val recipes: Recipes = parseRecipesResponse(response)
        return recipes
    }

    override suspend fun recipeSearch(query: String): Recipes {
        val response: HttpResponse = httpClient.get("https://api.spoonacular.com/recipes/complexSearch?apiKey=7423fe4fac8f428cbb46be6643272223&query=${query}")
        val recipes: Recipes = parseSearchRecipesResponse(response)
        return recipes
    }

    private suspend fun parseRecipesResponse(response: HttpResponse): Recipes {
        // Implement the logic to parse the response and convert it to a Recipes object
        // You can use the response.readText() or response.receive() method for conversion
        val json = Json {
            ignoreUnknownKeys = true // Ignore unknown keys during deserialization
            coerceInputValues = true // Coerce null values to default values
        }
        val responseString: String = response.body()
        return json.decodeFromString<Recipes>(responseString)
    }

    private suspend fun parseSearchRecipesResponse(response: HttpResponse): Recipes {
        // Implement the logic to parse the response and convert it to a Recipes object
        // You can use the response.readText() or response.receive() method for conversion
        val json = Json {
            ignoreUnknownKeys = true // Ignore unknown keys during deserialization
            coerceInputValues = true // Coerce null values to default values
        }
        val responseString: String = response.body()
        return json.decodeFromString<Recipes>(responseString)
    }
}

