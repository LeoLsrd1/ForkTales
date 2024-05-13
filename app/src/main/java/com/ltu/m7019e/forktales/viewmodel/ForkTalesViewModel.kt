package com.ltu.m7019e.forktales.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ltu.m7019e.forktales.ForkTalesApplication
import com.ltu.m7019e.forktales.database.CategoriesRepository
import com.ltu.m7019e.forktales.database.RecipesDetailsRepository
import com.ltu.m7019e.forktales.database.RecipesRepository
import com.ltu.m7019e.forktales.model.Category
import com.ltu.m7019e.forktales.model.Recipe
import com.ltu.m7019e.forktales.model.RecipeDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * Sealed interface representing the different states of the recipe list UI.
 */
sealed interface RecipeListUiState {

    data object Loading : RecipeListUiState

    /**
     * Represents the success state of the recipe list UI.
     *
     * @property recipes The list of recipes fetched successfully.
     */
    data class Success(val recipes: List<Recipe>) : RecipeListUiState

    data object Error : RecipeListUiState
}

/**
 * Sealed interface representing the different states of the recipe details UI.
 */
sealed interface RecipeDetailsUiState {

    data object Loading : RecipeDetailsUiState

    /**
     * Represents the success state of the recipe details UI.
     *
     * @property recipe The recipe details fetched successfully.
     */
    data class Success(val recipe: RecipeDetails) : RecipeDetailsUiState
    data object Error : RecipeDetailsUiState
}

/**
 * ViewModel for the ForkTales application.
 *
 * @property recipesRepository The repository for fetching recipes.
 * @property recipesDetailsRepository The repository for fetching recipe details.
 * @property categoriesRepository The repository for fetching categories.
 */
class ForkTalesViewModel(
    private val recipesRepository: RecipesRepository,
    private val recipesDetailsRepository: RecipesDetailsRepository,
    private val categoriesRepository: CategoriesRepository
): ViewModel() {
    var recipeListUiState: RecipeListUiState by mutableStateOf(RecipeListUiState.Loading)
    var recipeDetailsUiState: RecipeDetailsUiState by mutableStateOf(RecipeDetailsUiState.Loading)

    var selectedRecipeName: String = ""
    var categories: MutableStateFlow<List<Category>> = MutableStateFlow(emptyList())

    init {
        getCategoryList()
        observeCategoriesChanges()
    }

    /**
     * Observes changes in the categories and fetches recipes for the new categories.
     */
    private fun observeCategoriesChanges() {
        viewModelScope.launch {
            categories.collect { newCategories ->
                getRecipesByCategories(newCategories.map { it.strCategory })
            }
        }
    }

    /**
     * Fetches recipes for the given categories.
     *
     * @param categories The list of categories to fetch recipes for.
     */
    fun getRecipesByCategories(categories: List<String>) {
        recipeListUiState = RecipeListUiState.Loading
        viewModelScope.launch {
            recipeListUiState = try {
                val allRecipes = mutableListOf<Recipe>()
                categories.forEach { category ->
                    val recipes = recipesRepository.getRecipesByCategory(category)
                    allRecipes.addAll(recipes)
                }
                RecipeListUiState.Success(allRecipes)
            } catch (e: IOException) {
                RecipeListUiState.Error
            } catch (e: HttpException) {
                RecipeListUiState.Error
            }
        }
    }

    /**
     * Fetches the details of a recipe by its id.
     *
     * @param id The id of the recipe.
     */
    fun getRecipeDetailsById(id: String) {
        recipeDetailsUiState = RecipeDetailsUiState.Loading
        viewModelScope.launch {
            recipeDetailsUiState = try {
                val recipe = recipesDetailsRepository.getRecipeDetailsById(id)
                RecipeDetailsUiState.Success(recipe)
            } catch (e: IOException) {
                RecipeDetailsUiState.Error
            } catch (e: HttpException) {
                RecipeDetailsUiState.Error
            }
        }
    }

    /**
     * Fetches the list of categories.
     */
    fun getCategoryList() {
        viewModelScope.launch {
            try {
                categories.emit(categoriesRepository.getCategories())
            } catch (e: IOException) {
                Log.e("ForkTalesViewModel", e.toString())
            } catch (e: HttpException) {
                Log.e("ForkTalesViewModel", e.toString())
            }
        }
    }

    /**
     * Factory for creating instances of the ForkTalesViewModel.
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ForkTalesApplication)
                val recipesRepository = application.container.recipesRepository
                val recipesRepositoryDetails = application.container.recipesDetailsRepository
                val categoriesRepository = application.container.categoriesRepository
                ForkTalesViewModel(
                    recipesRepository = recipesRepository,
                    recipesDetailsRepository = recipesRepositoryDetails,
                    categoriesRepository = categoriesRepository
                )
            }
        }
    }
}