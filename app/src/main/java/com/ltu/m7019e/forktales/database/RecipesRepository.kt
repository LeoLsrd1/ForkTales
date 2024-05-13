package com.ltu.m7019e.forktales.database

import com.ltu.m7019e.forktales.model.Recipe
import com.ltu.m7019e.forktales.network.ForkTalesApiService

/**
 * An interface that defines the contract for a recipes repository.
 * It has a single function that fetches a list of recipes by category.
 */
interface RecipesRepository {
    /**
     * A suspend function that fetches a list of recipes by category.
     *
     * @param category The category of the recipes.
     * @return A list of Recipe objects.
     */
    suspend fun getRecipesByCategory(category: String): List<Recipe>
}

/**
 * A class that implements the RecipesRepository interface.
 * It uses a ForkTalesApiService to fetch the recipes from the network.
 *
 * @property apiService The ForkTalesApiService used to fetch the recipes.
 */
class NetworkRecipesRepository(
    private val apiService: ForkTalesApiService
) : RecipesRepository {
    /**
     * A suspend function that fetches a list of recipes by category from the network.
     * It also sets the category of each recipe to the provided category.
     *
     * @param category The category of the recipes.
     * @return A list of Recipe objects.
     */
    override suspend fun getRecipesByCategory(category: String): List<Recipe> {
        val recipes = apiService.getRecipesByCategory(category).meals
        recipes.forEach {
            it.strCategory = category
        }
        return recipes
    }
}