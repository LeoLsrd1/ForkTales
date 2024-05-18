package com.ltu.m7019e.forktales.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ltu.m7019e.forktales.R
import com.ltu.m7019e.forktales.model.Category
import com.ltu.m7019e.forktales.model.Recipe
import com.ltu.m7019e.forktales.viewmodel.FavoriteRecipesListUiState
import kotlinx.coroutines.flow.Flow

/**
 * This is a Composable function that displays the favorites screen of the application.
 * It displays a list of favorite recipes in different categories.
 *
 * @param favoriteRecipesListUiState The UI state of the favorite recipes list.
 * @param categoriesFlow The flow of categories.
 * @param modifier A Modifier that can be used to adjust the layout or other visual properties of the Composable.
 * @param onRecipeListItemClicked A function that is invoked when a favorite recipes list item is clicked.
 */
@Composable
fun FavoritesScreen(
    favoriteRecipesListUiState: FavoriteRecipesListUiState,
    categoriesFlow: Flow<List<Category>>,
    modifier: Modifier = Modifier,
    onRecipeListItemClicked: (Recipe) -> Unit
) {
    val categories by categoriesFlow.collectAsState(initial = emptyList())
    when (favoriteRecipesListUiState) {
        is FavoriteRecipesListUiState.Success -> {
            val recipes = favoriteRecipesListUiState.recipes
            val favorites: List<List<Recipe>> = getListsFavorites(
                recipes = recipes,
                categories = categories
            )
            if(favorites.isEmpty()) {
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(R.string.empty_favorites),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    items(favorites) { recipes ->
                        RecipeList(
                            recipeList = recipes,
                            title = recipes.first().strCategory,
                            modifier = modifier,
                            onRecipeListItemClicked = onRecipeListItemClicked
                        )
                    }
                }
            }
        }
        is FavoriteRecipesListUiState.Loading -> {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
        is FavoriteRecipesListUiState.Error -> {
            Text(
                text = "Error: Something went wrong!",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

/**
 * This is a Composable function that returns a list of lists of recipes filtered by their category.
 *
 * @param recipes The list of favorite recipes.
 * @param categories The list of categories.
 */
@Composable
fun getListsFavorites(
    recipes: List<Recipe>,
    categories: List<Category>,
): List<List<Recipe>> {
    var temp: List<Recipe>
    val recipesLists: MutableList<List<Recipe>> = mutableListOf()
    categories.forEach { category ->
        temp = recipes.filter { recipe ->
            recipe.strCategory == category.strCategory
        }
        if(temp.isNotEmpty()) {
            recipesLists.add(temp)
        }
    }
    return recipesLists
}