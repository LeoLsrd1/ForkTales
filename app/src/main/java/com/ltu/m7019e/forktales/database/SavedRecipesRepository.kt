package com.ltu.m7019e.forktales.database

import com.ltu.m7019e.forktales.model.RecipeDetails

/**
 * An interface that provides insert, delete, and retrieve [RecipeDetails] from a given data source.
 */
interface SavedRecipesRepository {
    /**
     * A suspend function that fetches the recipes from the data source.
     *
     * @return A list of RecipeDetails objects.
     */
    suspend fun getSavedRecipes(): List<RecipeDetails>

    /**
     * A suspend function that inserts a recipe in the data source.
     * @param recipeDetails The recipe to insert.
     */
    suspend fun insertRecipe(recipeDetails: RecipeDetails)

    /**
     *
     * A suspend function that fetches a recipe by its id from the data source.
     * @param idMeal The id of the recipe.
     * @return A RecipeDetails object.
     */
    suspend fun getRecipe(idMeal: String): RecipeDetails

    /**
     *
     * A suspend function that deletes a saved recipe from the data source.
     * @param recipeDetails The RecipeDetails object.
     */
    suspend fun deleteRecipe(recipeDetails: RecipeDetails)
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
     * @return A list of RecipeDetails objects.
     */
    override suspend fun getSavedRecipes(): List<RecipeDetails> {
        return recipeDao.getFavoriteRecipes()
    }

    /**
     * A suspend function that inserts a recipe in the data source.
     * @param recipeDetails The recipe to insert.
     */
    override suspend fun insertRecipe(recipeDetails: RecipeDetails) {
        recipeDao.insertFavoriteRecipe(recipeDetails)
    }

    /**
     *
     * A suspend function that fetches a recipe by its id from the data source.
     * @param idMeal The id of the recipe.
     * @return A RecipeDetails object.
     */
    override suspend fun getRecipe(idMeal: String): RecipeDetails {
        return recipeDao.getRecipe(idMeal)
    }

    /**
     *
     * A suspend function that deletes a saved recipe from the data source.
     * @param recipeDetails The RecipeDetails object.
     */
    override suspend fun deleteRecipe(recipeDetails: RecipeDetails) {
        recipeDao.deleteFavoriteRecipe(recipeDetails.idMeal)
    }
}