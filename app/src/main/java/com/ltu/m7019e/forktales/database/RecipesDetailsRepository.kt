package com.ltu.m7019e.forktales.database

import com.ltu.m7019e.forktales.model.RecipeDetails
import com.ltu.m7019e.forktales.network.ForkTalesApiService

/**
 * An interface that defines the contract for a recipes details repository.
 * It has two functions that fetch the details of a recipe by its id and fetch a random recipe.
 */
interface RecipesDetailsRepository {
    /**
     * A suspend function that fetches the details of a recipe by its id.
     *
     * @param id The id of the recipe.
     * @return A RecipeDetails object.
     */
    suspend fun getRecipeDetailsById(id: String): RecipeDetails

    /**
     * A suspend function that fetches a random recipe.
     *
     * @return A RecipeDetails object.
     */
    suspend fun getRandomRecipe(): RecipeDetails

    /**
     * A suspend function that searches for recipes by a query.
     *
     * @param query The query to search for.
     * @return A list of RecipeDetails objects.
     */
    suspend fun searchRecipes(query: String): List<RecipeDetails>
}

/**
 * A class that implements the RecipesDetailsRepository interface.
 * It uses a ForkTalesApiService to fetch the recipe details from the network.
 *
 * @property apiService The ForkTalesApiService used to fetch the recipe details.
 */
class NetworkRecipesDetailsRepository(
    private val apiService: ForkTalesApiService
) : RecipesDetailsRepository {
    /**
     * A suspend function that fetches the details of a recipe by its id from the network.
     *
     * @param id The id of the recipe.
     * @return A RecipeDetails object.
     */
    override suspend fun getRecipeDetailsById(id: String): RecipeDetails {
        return apiService.getRecipeDetailsById(id).meals.first()
    }

    /**
     * A suspend function that fetches a random recipe from the network.
     *
     * @return A RecipeDetails object.
     */
    override suspend fun getRandomRecipe(): RecipeDetails {
        return apiService.getRandomRecipe().meals.first()
    }

    /**
     * A suspend function that searches for recipes by a query from the network.
     *
     * @param query The query to search for.
     * @return A list of RecipeDetails objects.
     */
    override suspend fun searchRecipes(query: String): List<RecipeDetails> {
        return apiService.searchRecipes(query).meals
    }
}