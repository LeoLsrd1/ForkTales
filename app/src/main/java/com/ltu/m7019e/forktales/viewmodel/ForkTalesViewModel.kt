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
import com.ltu.m7019e.forktales.database.SavedRecipesRepository
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
 * Sealed interface representing the different states of the favorite recipes list UI.
 */
sealed interface FavoriteRecipesListUiState {

    data object Loading : FavoriteRecipesListUiState

    /**
     * Represents the success state of the favorite recipes list UI.
     *
     * @property recipes The list of recipes fetched successfully.
     */
    data class Success(val recipes: List<Recipe>) : FavoriteRecipesListUiState

    data object Error : FavoriteRecipesListUiState
}

/**
 * Sealed interface representing the different states of the recipe details UI.
 */
sealed interface RecipeDetailsUiState {

    data object Loading : RecipeDetailsUiState

    /**
     * Represents the success state of the recipe details UI.
     *
     * @property recipeDetails The recipe details fetched successfully.
     */
    data class Success(
        val recipeDetails: RecipeDetails,
        val isFavorite: Boolean) : RecipeDetailsUiState
    data object Error : RecipeDetailsUiState
}

/**
 * Sealed interface representing the different states of the recipe search UI.
 */
sealed interface SearchUiState {
    data object Empty : SearchUiState

    data object Loading : SearchUiState

    /**
     * Represents the success state of the recipe search UI.
     *
     * @property recipes The recipe details fetched successfully.
     */
    data class Success(val recipes: List<RecipeDetails>) : SearchUiState

    data object Error : SearchUiState
}

/**
 * ViewModel for the ForkTales application.
 *
 * @property recipesRepository The repository for fetching recipes.
 * @property recipesDetailsRepository The repository for fetching recipe details.
 * @property categoriesRepository The repository for fetching categories.
 * @property savedRecipesRepository The repository for saving recipes in the database.
 */
class ForkTalesViewModel(
    private val recipesRepository: RecipesRepository,
    private val recipesDetailsRepository: RecipesDetailsRepository,
    private val categoriesRepository: CategoriesRepository,
    private val savedRecipesRepository: SavedRecipesRepository
): ViewModel() {
    var recipeListUiState: RecipeListUiState by mutableStateOf(RecipeListUiState.Loading)
    var recipeDetailsUiState: RecipeDetailsUiState by mutableStateOf(RecipeDetailsUiState.Loading)
    var favoriteRecipesListUiState: FavoriteRecipesListUiState by mutableStateOf(FavoriteRecipesListUiState.Loading)
    var searchUiState: SearchUiState by mutableStateOf(SearchUiState.Empty)

    var selectedRecipeName: String = ""
    var categories: MutableStateFlow<List<Category>> = MutableStateFlow(emptyList())

    val searchValue = mutableStateOf("")

    init {
        getCategoryList()
        observeCategoriesChanges()
        updateFavoriteRecipes()
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
                val recipeDetails = recipesDetailsRepository.getRecipeDetailsById(id)
                RecipeDetailsUiState.Success(
                    recipeDetails,
                    savedRecipesRepository.getRecipe(id) != null)
            } catch (e: IOException) {
                RecipeDetailsUiState.Error
            } catch (e: HttpException) {
                RecipeDetailsUiState.Error
            }
        }
    }

    /**
     * Put the recipe details in the UI state.
     */
    fun getRecipeDetailsByObject(recipeDetails: RecipeDetails) {
        selectedRecipeName = recipeDetails.strMeal
        viewModelScope.launch {
            recipeDetailsUiState = RecipeDetailsUiState
                .Success(recipeDetails,
                    savedRecipesRepository.getRecipe(recipeDetails.idMeal) != null)
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
     * Updates the search value.
     */
    fun onSearchValueChange(newValue: String) {
        searchValue.value = newValue
    }

    /**
     * Fetches recipes by the search value.
     */
    fun getRecipesBySearchValue() {
        searchUiState = SearchUiState.Loading
        viewModelScope.launch {
            searchUiState = try {
                val recipes = recipesDetailsRepository.searchRecipes(searchValue.value)
                SearchUiState.Success(recipes)
            } catch (e: IOException) {
                SearchUiState.Error
            } catch (e: HttpException) {
                SearchUiState.Error
            }
        }
    }

    /**
     * Saves a RecipeDetails in the favorite recipes database.
     *
     * @param recipeDetails The RecipeDetails to save.
     */
    fun saveRecipe(recipeDetails: RecipeDetails) {
        viewModelScope.launch {
            savedRecipesRepository.insertRecipe(recipeDetails)
            updateFavoriteRecipes()
            recipeDetailsUiState = try {
                RecipeDetailsUiState.Success(
                    recipesDetailsRepository.getRecipeDetailsById(recipeDetails.idMeal),
                    true
                )
            } catch(e : IOException) {
                RecipeDetailsUiState.Error
            } catch(e : HttpException) {
                RecipeDetailsUiState.Error
            }
        }
    }

    /**
     * Deletes a RecipeDetails from the favorite recipes database.
     *
     * @param recipeDetails The RecipeDetails to delete.
     */
    fun deleteRecipe(recipeDetails: RecipeDetails) {
        viewModelScope.launch {
            savedRecipesRepository.deleteRecipe(recipeDetails)
            updateFavoriteRecipes()
            recipeDetailsUiState = try {
                RecipeDetailsUiState.Success(
                    recipesDetailsRepository.getRecipeDetailsById(recipeDetails.idMeal),
                    false
                )
            } catch(e : IOException) {
                RecipeDetailsUiState.Error
            } catch(e : HttpException) {
                RecipeDetailsUiState.Error
            }
        }
    }

    /**
     * Update the favorite recipes UI that displays the favorite recipes.
     */
    private fun updateFavoriteRecipes() {
        favoriteRecipesListUiState = FavoriteRecipesListUiState.Loading
        viewModelScope.launch {
            favoriteRecipesListUiState = try {
                val recipes: MutableList<Recipe> = mutableListOf()
                savedRecipesRepository.getSavedRecipes().forEach {
                    recipes.add(getRecipeFromRecipeDetails(it))
                }
                FavoriteRecipesListUiState.Success(recipes)
            } catch(e : IOException) {
                FavoriteRecipesListUiState.Error
            } catch(e : HttpException) {
                FavoriteRecipesListUiState.Error
            }
        }
    }

    /**
     *  Get a recipe from a recipe details.
     */
    private fun getRecipeFromRecipeDetails(recipeDetails: RecipeDetails): Recipe {
        return Recipe(
            idMeal = recipeDetails.idMeal,
            strMeal = recipeDetails.strMeal,
            strMealThumb = recipeDetails.strMealThumb,
            strCategory = recipeDetails.strCategory
        )
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
                val savedRecipesRepository = application.container.savedRecipesRepository
                ForkTalesViewModel(
                    recipesRepository = recipesRepository,
                    recipesDetailsRepository = recipesRepositoryDetails,
                    categoriesRepository = categoriesRepository,
                    savedRecipesRepository = savedRecipesRepository
                )
            }
        }
    }
}