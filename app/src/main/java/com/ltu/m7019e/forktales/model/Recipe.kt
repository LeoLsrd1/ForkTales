package com.ltu.m7019e.forktales.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val idMeal: String = "",
    val strMeal: String = "",
    val strMealThumb: String = "",
    var strCategory: String = "",
)
