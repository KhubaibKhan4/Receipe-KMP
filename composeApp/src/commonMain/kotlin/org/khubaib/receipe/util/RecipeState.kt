package org.khubaib.receipe.util

import org.khubaib.receipe.data.model.Recipes

sealed class RecipeState{
    object Loading: RecipeState()
    data class Success(val recipes: Recipes): RecipeState()
    data class Error(val error: String) : RecipeState()
}
