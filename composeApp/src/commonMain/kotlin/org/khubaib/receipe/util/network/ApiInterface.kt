package org.khubaib.receipe.util.network

import org.khubaib.receipe.data.model.Recipes

interface ApiInterface{
    suspend fun recipeRandom(): Recipes

    suspend fun recipeSearch(query: String): Recipes
}