package org.khubaib.receipe.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Step(
    @SerialName("equipment")
    val equipment: List<Equipment>,
    @SerialName("ingredients")
    val ingredients: List<Ingredient>,
    @SerialName("length")
    val length: Length? =null,
    @SerialName("number")
    val number: Int,
    @SerialName("step")
    val step: String
)