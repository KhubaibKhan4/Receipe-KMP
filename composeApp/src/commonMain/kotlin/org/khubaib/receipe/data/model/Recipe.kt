package org.khubaib.receipe.data.model

data class Recipe(
    val title: String,
    val description: String,
    val imageUrl: String,
    val cookingTime: Int
)