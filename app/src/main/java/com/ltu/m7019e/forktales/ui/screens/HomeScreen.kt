package com.ltu.m7019e.forktales.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Card
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

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val recipes = Recipes().getRecipes()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                RecipeList(recipeList = recipes, title = "Sea Food", modifier = modifier)
                Spacer(modifier = Modifier.size(32.dp))
                RecipeList(recipeList = recipes, title = "Meat", modifier = modifier)
                Spacer(modifier = Modifier.size(32.dp))
                RecipeList(recipeList = recipes, title = "Vegetarian", modifier = modifier)
            }
        }
    }
}

@Composable
fun RecipeList(
    modifier: Modifier = Modifier,
    title: String = "",
    recipeList: List<Recipe>,
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
                RecipeCard(recipe = recipe)
            }
        }
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe
) {
    Card(
        modifier = Modifier
            .size(200.dp, 290.dp)
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
                    maxLines = 2
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "${recipe.strCategory} - ${recipe.strArea}",
                    style = MaterialTheme.typography.bodyMedium
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
        )
    )
}
