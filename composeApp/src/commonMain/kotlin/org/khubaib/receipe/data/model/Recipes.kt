package org.khubaib.receipe.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Recipes(
    @SerialName("meals")
    val meals: List<Meal>
)