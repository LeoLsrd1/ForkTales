package com.ltu.m7019e.forktales.test

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.ltu.m7019e.forktales.database.RecipeDao
import com.ltu.m7019e.forktales.database.RecipeDatabase
import com.ltu.m7019e.forktales.model.RecipeDetails
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ForkTalesDatabaseTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var database: RecipeDatabase
    private lateinit var recipeDao: RecipeDao

    @Before
    fun setForkTalesDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RecipeDatabase::class.java).build()
        recipeDao = database.recipeDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun forkTalesDatabase_saveFavoriteRecipeDetails_successfullySavesToDatabase() = runTest {
        val recipeDetails = RecipeDetails(idMeal = "123")

            recipeDao.insertFavoriteRecipe(recipeDetails)

            val savedRecipeDetails = recipeDao.getRecipe("123")

            assertThat(savedRecipeDetails, equalTo(recipeDetails))
    }

    @Test
    fun forkTalesDatabase_deleteFavoriteRecipeDetails_successfullyDeletesFromDatabase() = runTest {
        val recipeDetails = RecipeDetails(idMeal = "123")

        recipeDao.insertFavoriteRecipe(recipeDetails)

        val favoritesBeforeDelete = recipeDao.getFavoriteRecipes()

        assertTrue(favoritesBeforeDelete.isNotEmpty())

        recipeDao.deleteFavoriteRecipe(recipeDetails.idMeal)

        val favoritesAfterDelete = recipeDao.getFavoriteRecipes()

        assertTrue(favoritesAfterDelete.isEmpty())
    }

    @Test
    fun forkTalesDatabase_getOneFavoriteRecipeDetails_successfullyGetsOneFavoriteRecipeFromDatabase() = runTest {
        val recipeDetails = RecipeDetails(idMeal = "123")

        recipeDao.insertFavoriteRecipe(recipeDetails)

        val recipeDetailsExpected = recipeDao.getRecipe(recipeDetails.idMeal)

        assertThat(recipeDetailsExpected, equalTo(recipeDetails))
    }

    @Test
    fun forkTalesDatabase_getFavoriteRecipeDetails_successfullyGetsFavoriteRecipesFromDatabase() = runTest {
        recipeDao.insertFavoriteRecipe(RecipeDetails(idMeal = "1"))
        recipeDao.insertFavoriteRecipe(RecipeDetails(idMeal = "2"))
        recipeDao.insertFavoriteRecipe(RecipeDetails(idMeal = "3"))

        val favorites = recipeDao.getFavoriteRecipes()

        assertTrue(favorites.size == 3)
    }
}