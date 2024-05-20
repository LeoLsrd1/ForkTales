package com.ltu.m7019e.forktales.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ltu.m7019e.forktales.R
import com.ltu.m7019e.forktales.model.RecipeDetails
import com.ltu.m7019e.forktales.viewmodel.ForkTalesViewModel
import com.ltu.m7019e.forktales.viewmodel.SearchUiState

/**
 * This is a Composable function that displays the search screen of the application.
 * It displays a grid of recipes that match the search query.
 *
 * @param viewModel The ViewModel that provides the data for the screen.
 * @param onRecipeListItemClicked A function that is invoked when a recipe list item is clicked.
 * @param windowSize The size of the window.
 * @param modifier A Modifier that can be used to adjust the layout or other visual properties of the Composable.
 */
@Composable
fun SearchGridScreen(
    viewModel: ForkTalesViewModel,
    onRecipeListItemClicked: (RecipeDetails) -> Unit,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
) {
    val searchValue by viewModel.searchValue
    val searchUiState = viewModel.searchUiState

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        TextField(
            value = searchValue,
            onValueChange = { viewModel.onSearchValueChange(it) },
            label = { Text(stringResource(id = R.string.search)) },
            singleLine = true,
            trailingIcon = {
                if (searchValue.isNotEmpty()) {
                    IconButton(onClick = {
                        viewModel.onSearchValueChange("")
                    }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                viewModel.getRecipesBySearchValue()
                keyboardController?.hide()
                focusManager.clearFocus()
            }),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
        when (searchUiState) {
            is SearchUiState.Success -> {
                val nbColumns = if(windowSize != WindowWidthSizeClass.Expanded) 2 else 4

                if(searchUiState.recipes.isEmpty()) {
                    Box(contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "No results found!",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                } else {
                    RecipesGrid(
                        columnNumber = nbColumns,
                        recipes = searchUiState.recipes,
                        onRecipeListItemClicked = onRecipeListItemClicked,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
            is SearchUiState.Empty -> {
            }
            is SearchUiState.Loading -> {
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
            is SearchUiState.Error -> {
                Text(
                    text = "Error: Something went wrong!",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

/**
 * This is a Composable function that displays a grid of recipes.
 * It takes a number of columns, a list of recipes, a modifier, and a function to be invoked when a recipe list item is clicked.
 *
 * @param columnNumber The number of columns in the grid.
 * @param recipes The list of recipes to be displayed.
 * @param modifier A Modifier that can be used to adjust the layout or other visual properties of the Composable.
 * @param onRecipeListItemClicked A function that is invoked when a recipe list item is clicked.
 */
@Composable
fun RecipesGrid(
    columnNumber: Int,
    recipes: List<RecipeDetails>,
    modifier: Modifier = Modifier,
    onRecipeListItemClicked: (RecipeDetails) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnNumber),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        items(recipes) { recipe ->
            RecipeGridCard(
                recipe = recipe,
                onRecipeListItemClicked = onRecipeListItemClicked
            )
        }
    }
}

/**
 * This is a Composable function that displays a recipe card in a grid.
 * It takes a recipe and a function to be invoked when the recipe card is clicked.
 *
 * @param recipe The recipe to be displayed.
 * @param modifier A Modifier that can be used to adjust the layout or other visual properties of the Composable.
 * @param onRecipeListItemClicked A function that is invoked when the recipe card is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeGridCard(
    recipe: RecipeDetails,
    modifier: Modifier = Modifier,
    onRecipeListItemClicked: (RecipeDetails) -> Unit
) {
    Card(
        onClick = { onRecipeListItemClicked(recipe)},
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                AsyncImage(
                    model = recipe.strMealThumb,
                    contentDescription = recipe.strMeal,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = recipe.strMeal,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    minLines = 2
                )
            }
        }
    }
}
