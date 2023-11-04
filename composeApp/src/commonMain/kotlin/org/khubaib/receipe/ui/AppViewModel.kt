package org.khubaib.receipe.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import org.khubaib.receipe.data.model.RecipeX
import org.khubaib.receipe.data.model.Recipes
import org.khubaib.receipe.data.repository.RecipeRepository
import org.khubaib.receipe.util.network.DataState

class AppViewModel(private val httpClient: HttpClient) : ViewModel() {


    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    private val repo = RecipeRepository(httpClient)

    val recipeData: MutableState<DataState<Recipes>?> = mutableStateOf(null)
    val recipesData = MutableStateFlow<DataState<List<RecipeX>?>>(DataState.Loading)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun recipeApi() {
        viewModelScope.launch {
            flowOf(Recipes).debounce(300)
                .distinctUntilChanged()
                .flatMapLatest {
                    repo.recipeRandom()
                }.collect {
                    if (it is DataState.Success) {
                        it.data
                    }
                }
        }
    }

    fun recipeRandom() {
        viewModelScope.launch(Dispatchers.Main) {
            repo.recipeRandom().collectLatest {
                recipesData.value = it
            }
        }
    }

    fun recipeSearch(query: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repo.recipeSearch(query).collectLatest {
                recipesData.value = it
            }
        }
    }

}