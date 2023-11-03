package org.khubaib.receipe.util

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import org.khubaib.receipe.data.model.RecipeX
import org.khubaib.receipe.data.model.Recipes
import org.khubaib.receipe.data.repository.RecipeRepository
import org.khubaib.receipe.util.network.DataState

class RandomViewModel(private val httpClient: HttpClient): ViewModel() {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    private val repo = RecipeRepository(httpClient)
    val randomRecipe = MutableStateFlow<DataState<Recipes>?>(DataState.Loading)

//    fun randomRecipes() {
//        viewModelScope.launch(Dispatchers.Main) {
//            repo.recipeRandom().collectLatest {
//                randomRecipe.value = it
//            }
//        }
//    }

    fun randomRecipes() {
        viewModelScope.launch(Dispatchers.Main) {
            repo.recipeRandom().collectLatest {
                when (it) {
                    is DataState.Success -> {
                        randomRecipe.value = DataState.Success(Recipes(it.data))
                    }
                    is DataState.Error -> {
                        randomRecipe.value = DataState.Error(it.exception)
                    }
                    is DataState.Loading -> {
                        randomRecipe.value = DataState.Loading
                    }
                }
            }
        }
    }

}