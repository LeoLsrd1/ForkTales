package com.ltu.m7019e.forktales.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import com.ltu.m7019e.forktales.R
import com.ltu.m7019e.forktales.model.RecipeDetails
import com.ltu.m7019e.forktales.utils.Constants
import com.ltu.m7019e.forktales.viewmodel.ForkTalesViewModel
import com.ltu.m7019e.forktales.viewmodel.RecipeDetailsUiState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

/**
 * This is a Composable function that displays the details of a selected recipe.
 * It uses the ForkTalesViewModel to get the selected recipe and displays its details.
 * It also provides a navigation up function to go back to the previous screen.
 *
 * @param forkTalesViewModel The View Model.
 * @param recipeName The name of the recipe.
 * @param navigateUp A function that navigates up in the navigation stack.
 * @param windowSize The size of the window.
 * @param modifier A Modifier that can be used to adjust the layout or other visual properties of the Composable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    forkTalesViewModel: ForkTalesViewModel,
    recipeName: String,
    navigateUp: () -> Unit,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Column(modifier = modifier) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = recipeName,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = {
                IconButton(onClick = { navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        )
        when (val recipeDetailsUiState = forkTalesViewModel.recipeDetailsUiState) {
            is RecipeDetailsUiState.Success -> {
                val recipeDetails = recipeDetailsUiState.recipeDetails
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    item {
                        Box {
                            AsyncImage(
                                model = recipeDetails.strMealThumb,
                                contentDescription = recipeDetails.strMeal,
                                contentScale = ContentScale.Crop,
                                modifier =
                                if(windowSize != WindowWidthSizeClass.Expanded) {
                                    Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(4 / 3f)
                                } else {
                                    Modifier
                                        .fillMaxWidth(0.5f)
                                        .aspectRatio(4 / 3f)
                                }
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (recipeDetails.strArea != "" && recipeDetails.strTags != "") {
                                Text(
                                    text = "${recipeDetails.strArea} | ${recipeDetails.strTags}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            } else {
                                Text(
                                    text = "${recipeDetails.strArea}${recipeDetails.strTags}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            if (!recipeDetailsUiState.isFavorite) {
                                IconButton(
                                    onClick = { forkTalesViewModel.saveRecipe(recipeDetails) }
                                ) {
                                    Icon(
                                        Icons.Filled.FavoriteBorder,
                                        contentDescription = null
                                    )
                                }
                            } else {
                                IconButton(
                                    onClick = { forkTalesViewModel.deleteRecipe(recipeDetails) }
                                ) {
                                    androidx.compose.material3.Icon(
                                        Icons.Filled.Favorite,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                        
                    }

                    item {
                        Text(
                            text = stringResource(R.string.ingredients),
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        val nbChunks = if(windowSize != WindowWidthSizeClass.Expanded) 3 else 6
                        IngredientsGrid(recipe = recipeDetails, nbChunks = nbChunks)
                    }

                    item {
                        Text(
                            text = "Instructions",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = recipeDetails.strInstructions,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(8.dp),
                            )
                        }
                    }

                    item {
                        if(recipeDetails.strYoutube != "") {
                            YoutubePlayer(
                                videoId = recipeDetails.strYoutube.split("v=")[1],
                                lifecycleOwner = context as LifecycleOwner,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }

                    item {
                        Text(
                            text = stringResource(R.string.youtube),
                            style = MaterialTheme.typography.bodySmall,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recipeDetails.strSource))
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }

            RecipeDetailsUiState.Loading -> {
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }

            RecipeDetailsUiState.Error -> {
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
 * This is a Composable function that displays a grid of ingredients for a given recipe.
 * It takes a RecipeDetails object as a parameter and displays its ingredients in a grid format.
 *
 * @param recipe The RecipeDetails object whose ingredients are to be displayed.
 * @param nbChunks The number of ingredients to display on one row.
 */
@Composable
fun IngredientsGrid(
    recipe: RecipeDetails,
    nbChunks: Int,
) {
    val ingredients = listOf(
        recipe.strIngredient1 to recipe.strMeasure1,
        recipe.strIngredient2 to recipe.strMeasure2,
        recipe.strIngredient3 to recipe.strMeasure3,
        recipe.strIngredient4 to recipe.strMeasure4,
        recipe.strIngredient5 to recipe.strMeasure5,
        recipe.strIngredient6 to recipe.strMeasure6,
        recipe.strIngredient7 to recipe.strMeasure7,
        recipe.strIngredient8 to recipe.strMeasure8,
        recipe.strIngredient9 to recipe.strMeasure9,
        recipe.strIngredient10 to recipe.strMeasure10,
        recipe.strIngredient11 to recipe.strMeasure11,
        recipe.strIngredient12 to recipe.strMeasure12,
        recipe.strIngredient13 to recipe.strMeasure13,
        recipe.strIngredient14 to recipe.strMeasure14,
        recipe.strIngredient15 to recipe.strMeasure15,
        recipe.strIngredient16 to recipe.strMeasure16,
        recipe.strIngredient17 to recipe.strMeasure17,
        recipe.strIngredient18 to recipe.strMeasure18,
        recipe.strIngredient19 to recipe.strMeasure19,
        recipe.strIngredient20 to recipe.strMeasure20
    )

    val gridRows = ingredients.chunked(nbChunks)
    gridRows.forEach { row ->
        val maxWidthFraction = row.size / 3f
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(maxWidthFraction)
        ) {
            row.forEach { (ingredient, measure) ->
                if(ingredient != "") {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f)
                    ) {
                        IngredientCard(ingredient, measure)
                    }
                } else {
                    Box (
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1f)
                    )
                }
            }
        }
    }
}

/**
 * This is a Composable function that displays an ingredient card.
 * It takes an ingredient and its measure as parameters and displays them in a card format.
 *
 * @param ingredient The ingredient to be displayed.
 * @param measure The measure of the ingredient to be displayed.
 */
@Composable
fun IngredientCard(
    ingredient: String,
    measure: String
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card {
            Box(
                modifier = Modifier.padding(8.dp)
            ) {
                AsyncImage(
                    model = Constants.INGREDIENT_IMAGE + ingredient + "-Small.png",
                    contentDescription = ingredient,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = measure,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = ingredient,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * This is a Composable function that displays a YouTube player.
 * It takes a videoId and a lifecycleOwner as parameters and displays the YouTube player.
 *
 * @param videoId The videoId of the YouTube video to be displayed.
 * @param lifecycleOwner The lifecycleOwner of the Composable.
 * @param modifier A Modifier that can be used to adjust the layout or other visual properties of the Composable.
 */
@Composable
fun YoutubePlayer(
    videoId: String,
    lifecycleOwner: LifecycleOwner,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier.clip(MaterialTheme.shapes.medium),
        factory = {context ->
        YouTubePlayerView(context = context).apply {
            lifecycleOwner.lifecycle.addObserver(this)
            addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            })
        }
    })
}