package com.ltu.m7019e.forktales.test

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.ltu.m7019e.forktales.ForkTalesApp
import com.ltu.m7019e.forktales.ForkTalesNavScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ForkTalesScreenNavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setForkTalesNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            ForkTalesApp(
                windowSize = WindowWidthSizeClass.Expanded,
                navController = navController
            )
        }
    }

    @Test
    fun forkTalesNavHost_verifyStartDestination() {
        navController.assertCurrentRouteName(ForkTalesNavScreen.Home.route)
    }

    @Test
    fun forkTalesNavHost_verifyBackNavigationNotShownOnStartOrderScreen() {
        val backText = composeTestRule.activity.getString(com.ltu.m7019e.forktales.R.string.back)
        composeTestRule.onNodeWithContentDescription(backText).assertDoesNotExist()
    }

    @Test
    fun forkTalesNavHost_clickFavorites_navigatesToFavorites() {
        composeTestRule.onNodeWithStringId(com.ltu.m7019e.forktales.R.string.favorites).performClick()
        navController.assertCurrentRouteName(ForkTalesNavScreen.Favorites.route)
    }

    @Test
    fun forkTalesNavHost_clickSearch_navigatesToSearch() {
        composeTestRule.onNodeWithStringId(com.ltu.m7019e.forktales.R.string.search).performClick()
        navController.assertCurrentRouteName(ForkTalesNavScreen.Search.route)
    }

}