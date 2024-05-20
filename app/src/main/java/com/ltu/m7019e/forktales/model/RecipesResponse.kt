package com.ltu.m7019e.forktales.model

import kotlinx.serialization.Serializable

@Serializable
data class RecipesResponse(
    val meals: List<Recipe> = listOf()
)
