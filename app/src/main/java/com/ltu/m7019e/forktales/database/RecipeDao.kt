package com.ltu.m7019e.forktales.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ltu.m7019e.forktales.model.Recipe

/**
 * An interface that provides insert, delete, and retrieve [Recipe] from a given data source.
 */
@Dao
interface RecipeDao {
    /**
     * Fetches all favorite recipes from the data source.
     *
     * @return A list of Recipe objects representing all favorite recipes.
     */
    @Query("SELECT * FROM favorite_recipes")
    suspend fun getFavoriteRecipes(): List<Recipe>

    /**
     * Inserts a new favorite recipe into the data source.
     * If the recipe already exists, the insertion is ignored.
     *
     * @param recipe The Recipe object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteRecipe(recipe: Recipe)

    /**
     * Fetches a favorite recipe by its id.
     *
     * @param idMeal The id of the recipe to fetch.
     * @return The Recipe object corresponding to the given id.
     */
    @Query("SELECT * FROM favorite_recipes where idMeal = :idMeal")
    suspend fun getRecipe(idMeal: String): Recipe

    /**
     * Deletes a favorite recipe from the data source by its it.
     *
     * @param idMeal The id of the recipe to delete.
     */
    @Query("DELETE FROM favorite_recipes where idMeal = :idMeal")
    suspend fun deleteFavoriteRecipe(idMeal: String)
}