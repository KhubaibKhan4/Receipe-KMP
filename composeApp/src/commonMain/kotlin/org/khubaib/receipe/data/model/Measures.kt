package org.khubaib.receipe.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Measures(
    @SerialName("metric")
    val metric: Metric,
    @SerialName("us")
    val us: Us
)