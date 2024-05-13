package com.ltu.m7019e.forktales.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val strCategory: String = "",
    val strCategoryThumb: String = "",
    val strCategoryDescription: String = ""
)
