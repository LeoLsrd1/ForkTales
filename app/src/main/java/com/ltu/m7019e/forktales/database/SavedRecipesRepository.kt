package com.ltu.m7019e.forktales.database

import com.ltu.m7019e.forktales.model.Recipe

/**
 * An interface that provides insert, delete, and retrieve [Recipe] from a given data source.
 */
interface SavedRecipesRepository {
    /**
     * A suspend function that fetches the recipes from the data source.
     *
     * @return A list of Recipe objects.
     */
    suspend fun getSavedRecipes(): List<Recipe>

    /**
     * A suspend function that inserts a recipe in the data source.
     * @param recipe The recipe to insert.
     */
    suspend fun insertRecipe(recipe: Recipe)

    /**
     *
     * A suspend function that fetches a recipe by its id from the data source.
     * @param idMeal The id of the recipe.
     * @return A Recipe object.
     */
    suspend fun getRecipe(idMeal: String): Recipe

    /**
     *
     * A suspend function that deletes a saved recipe from the data source.
     * @param recipe The Recipe object.
     */
    suspend fun deleteRecipe(recipe: Recipe)
}

/**
 * A class that implements the SavedRecipesRepository interface.
 * It uses a RecipeDao to handle the saved recipes from the data source.
 *
 * @property recipeDao The RecipeDao used to handle the saved recipes from the data source.
 */
class FavoriteRecipesRepository(private val recipeDao: RecipeDao) : SavedRecipesRepository {
    /**
     * A suspend function that fetches the recipes from the data source.
     *
     * @return A list of Recipe objects.
     */
    override suspend fun getSavedRecipes(): List<Recipe> {
        return recipeDao.getFavoriteRecipes()
    }

    /**
     * A suspend function that inserts a recipe in the data source.
     * @param recipe The recipe to insert.
     */
    override suspend fun insertRecipe(recipe: Recipe) {
        recipeDao.insertFavoriteRecipe(recipe)
    }

    /**
     *
     * A suspend function that fetches a recipe by its id from the data source.
     * @param idMeal The id of the recipe.
     * @return A Recipe object.
     */
    override suspend fun getRecipe(idMeal: String): Recipe {
        return recipeDao.getRecipe(idMeal)
    }

    /**
     *
     * A suspend function that deletes a saved recipe from the data source.
     * @param recipe The Recipe object.
     */
    override suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteFavoriteRecipe(recipe.idMeal)
    }
}