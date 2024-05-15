package com.ltu.m7019e.forktales.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "favorite_recipes")
data class Recipe(
    @PrimaryKey
    val idMeal: String = "",
    val strMeal: String = "",
    val strMealThumb: String = "",
    var strCategory: String = "",
)
