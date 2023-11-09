package org.khubaib.receipe.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.khubaib.receipe.repository.Repository
import org.khubaib.receipe.util.RecipeState

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _recipes = MutableStateFlow<RecipeState>(RecipeState.Loading)
    val randomRecipes: StateFlow<RecipeState> = _recipes

    private val _searchRecipe = MutableStateFlow<RecipeState>(RecipeState.Loading)
    val searchRecipes: StateFlow<RecipeState> = _searchRecipe

    private val _countryRecipe = MutableStateFlow<RecipeState>(RecipeState.Loading)
    val countryRecipe: StateFlow<RecipeState> = _countryRecipe


    fun getRandomRecipes() {
        viewModelScope.launch {
            _recipes.value = RecipeState.Loading
            try {
                val response = repository.getRandomRecipe()
                _recipes.value = RecipeState.Success(response)
            } catch (e: Exception) {
                _recipes.value = RecipeState.Error(e.message.toString())
            }
        }
    }

    fun getSearchRecipes(query: String) {
        viewModelScope.launch {
            _searchRecipe.value = RecipeState.Loading
            try {
                val response = repository.getSearchRecipe(query)
                _searchRecipe.value = RecipeState.Success(response)
            } catch (e: Exception) {
                _searchRecipe.value = RecipeState.Error(e.message.toString())
            }
        }
    }

    fun getCountryRecipes(country: String) {
        viewModelScope.launch {
            _countryRecipe.value = RecipeState.Loading
            try {
                val response = repository.getSearchRecipe(country)
                _countryRecipe.value = RecipeState.Success(response)
            } catch (e: Exception) {
                _countryRecipe.value = RecipeState.Error(e.message.toString())
            }
        }
    }

}