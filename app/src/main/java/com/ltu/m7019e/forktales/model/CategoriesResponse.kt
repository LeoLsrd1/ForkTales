package com.ltu.m7019e.forktales.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoriesResponse(
    val categories: List<Category> = listOf()
)
