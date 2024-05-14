package com.ltu.m7019e.forktales.network

import com.ltu.m7019e.forktales.model.CategoriesResponse
import com.ltu.m7019e.forktales.model.RecipeDetailsResponse
import com.ltu.m7019e.forktales.model.RecipesResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * An interface that defines the contract for the ForkTales API service.
 * It has functions to fetch recipes by category, fetch a random recipe, fetch recipe details by id, and fetch categories.
 */
interface ForkTalesApiService {

    /**
     * A suspend function that fetches a list of recipes by category from the network.
     *
     * @param category The category of the recipes.
     * @return A RecipesResponse object.
     */
    @GET("filter.php")
    suspend fun getRecipesByCategory(
        @Query("c")
        category: String
    ): RecipesResponse

    /**
     * A suspend function that fetches a random recipe from the network.
     *
     * @return A RecipeDetailsResponse object.
     */
    @GET("random.php")
    suspend fun getRandomRecipe(): RecipeDetailsResponse

    /**
     * A suspend function that fetches the details of a recipe by its id from the network.
     *
     * @param id The id of the recipe.
     * @return A RecipeDetailsResponse object.
     */
    @GET("lookup.php")
    suspend fun getRecipeDetailsById(
        @Query("i")
        id: String
    ): RecipeDetailsResponse

    /**
     * A suspend function that fetches a list of categories from the network.
     *
     * @return A CategoriesResponse object.
     */
    @GET("categories.php")
    suspend fun getCategories(): CategoriesResponse

    /**
     * A suspend function that searches for recipes by a query from the network.
     *
     * @param query The query to search for.
     * @return A RecipeDetailsResponse object.
     */
    @GET("search.php")
    suspend fun searchRecipes(
        @Query("s")
        query: String
    ): RecipeDetailsResponse
}