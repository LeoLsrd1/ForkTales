package com.ltu.m7019e.forktales.model

import kotlinx.serialization.Serializable

@Serializable
data class RecipeDetailsResponse(
    val meals: List<RecipeDetails>
)
