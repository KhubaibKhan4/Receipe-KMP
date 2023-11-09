package org.khubaib.receipe.repository

import org.khubaib.receipe.data.model.Recipes
import org.khubaib.receipe.data.remote.RecipeClientApi

class Repository {

    suspend fun getRandomRecipe(): Recipes {
        return RecipeClientApi.getRecipes()
    }

    suspend fun getSearchRecipe(query: String): Recipes {
        return RecipeClientApi.getSearchRecipes(query)
    }

    suspend fun getCountryRecipe(country: String): Recipes {
        return RecipeClientApi.getCountryRecipes(country)
    }

}