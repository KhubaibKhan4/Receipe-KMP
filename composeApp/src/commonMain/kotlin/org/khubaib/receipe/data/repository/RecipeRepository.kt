package org.khubaib.receipe.data.repository

import androidx.compose.runtime.MutableState
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.flow
import org.khubaib.receipe.data.model.Recipes
import org.khubaib.receipe.util.network.ApiImp
import org.khubaib.receipe.util.network.DataState

//class RecipeRepository {
//
//    private val api = ApiImp()
//
//    fun recipeRandom() = flow {
//        emit(DataState.Loading)
//        try {
//            val result = api.recipeRandom()
//            emit(DataState.Success(result.recipes))
//        } catch (e: Exception) {
//            emit(DataState.Error(e))
//        }
//    }
//
//}

class RecipeRepository(private val httpClient: HttpClient) {



    private val api = ApiImp(httpClient)

    fun recipeRandom() = flow {
        emit(DataState.Loading)
        try {
            val result = api.recipeRandom()
            emit(DataState.Success(result.recipes))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    fun recipeSearch(query: String) = flow {
        emit(DataState.Loading)
        try {
            val result = api.recipeSearch(query)
            emit(DataState.Success(result.recipes))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}
