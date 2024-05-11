package com.ltu.m7019e.forktales.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ltu.m7019e.forktales.model.Recipe
import coil.compose.AsyncImage
import com.ltu.m7019e.forktales.database.Recipes

/**
 * This is a Composable function that displays the home screen of the application.
 * It displays a list of recipes in different categories.
 *
 * @param modifier A Modifier that can be used to adjust the layout or other visual properties of the Composable.
 * @param onRecipeListItemClicked A function that is invoked when a recipe list item is clicked.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onRecipeListItemClicked: (Recipe) -> Unit
) {
    val recipes = Recipes().getRecipes()
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            RecipeList(
                recipeList = recipes,
                title = "Sea Food",
                modifier = modifier,
                onRecipeListItemClicked = onRecipeListItemClicked
            )
            Spacer(modifier = Modifier.size(32.dp))
            RecipeList(
                recipeList = recipes,
                title = "Meat",
                modifier = modifier,
                onRecipeListItemClicked = onRecipeListItemClicked
            )
            Spacer(modifier = Modifier.size(32.dp))
            RecipeList(
                recipeList = recipes,
                title = "Vegetarian",
                modifier = modifier,
                onRecipeListItemClicked = onRecipeListItemClicked
            )
            Spacer(modifier = Modifier.size(32.dp))
            RecipeList(
                recipeList = recipes,
                title = "Discover",
                modifier = modifier,
                onRecipeListItemClicked = onRecipeListItemClicked
            )
        }
    }

}

/**
 * This is a Composable function that displays a list of recipes.
 * It takes a list of recipes, a title, a modifier, and a function to be invoked when a recipe list item is clicked.
 *
 * @param modifier A Modifier that can be used to adjust the layout or other visual properties of the Composable.
 * @param title The title of the recipe list.
 * @param recipeList The list of recipes to be displayed.
 * @param onRecipeListItemClicked A function that is invoked when a recipe list item is clicked.
 */
@Composable
fun RecipeList(
    modifier: Modifier = Modifier,
    title: String = "",
    recipeList: List<Recipe>,
    onRecipeListItemClicked: (Recipe) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(recipeList) { recipe ->
                RecipeCard(recipe = recipe, onRecipeListItemClicked = onRecipeListItemClicked)
            }
        }
    }
}

/**
 * This is a Composable function that displays a recipe card.
 * It takes a recipe and a function to be invoked when the recipe card is clicked.
 *
 * @param recipe The recipe to be displayed.
 * @param onRecipeListItemClicked A function that is invoked when the recipe card is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeCard(
    recipe: Recipe,
    onRecipeListItemClicked: (Recipe) -> Unit
) {
    Card(
        modifier = Modifier
            .size(200.dp, 290.dp),
        onClick = {
            onRecipeListItemClicked(recipe)
        }
    ) {
        Column {
            Box {
                AsyncImage(
                    model = recipe.strMealThumb,
                    contentDescription = recipe.strMeal,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(200.dp)
                )
            }
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = recipe.strMeal,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "${recipe.strArea} - ${recipe.strTags}",
                    style = MaterialTheme.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewRecipeCard() {
    RecipeCard(
        recipe = Recipe(
            idMeal = 1,
            strMeal = "Spaghetti Carbonara",
            strCategory = "Pasta",
            strArea = "Italian",
            strInstructions = "Cook the pasta, fry the bacon, mix the eggs and cheese, combine everything",
            strMealThumb = "https://www.themealdb.com/images/media/meals/58oia61564916529.jpg"
        ),
        onRecipeListItemClicked = {}
    )
}