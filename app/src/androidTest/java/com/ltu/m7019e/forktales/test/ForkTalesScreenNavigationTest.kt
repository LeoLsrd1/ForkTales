package com.ltu.m7019e.forktales.test

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.ltu.m7019e.forktales.ForkTalesApp
import com.ltu.m7019e.forktales.ForkTalesNavScreen
import com.ltu.m7019e.forktales.R
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
        navController.assertCurrentRouteName(ForkTalesNavScreen.Login.route)
    }

    @Test
    fun forkTalesNavHost_loginNavigatesToHome() {
        login()
        navController.assertCurrentRouteName(ForkTalesNavScreen.Home.route)
    }

    @Test
    fun forkTalesNavHost_verifyBackNavigationNotShownOnHomeScreen() {
        login()
        val backText = composeTestRule.activity.getString(R.string.back)
        composeTestRule.onNodeWithContentDescription(backText).assertDoesNotExist()
    }

    @Test
    fun forkTalesNavHost_clickFavorites_navigatesToFavorites() {
        login()
        composeTestRule.onNodeWithStringId(R.string.favorites).performClick()
        navController.assertCurrentRouteName(ForkTalesNavScreen.Favorites.route)
    }

    @Test
    fun forkTalesNavHost_clickSearch_navigatesToSearch() {
        login()
        composeTestRule.onNodeWithStringId(R.string.search).performClick()
        navController.assertCurrentRouteName(ForkTalesNavScreen.Search.route)
    }

    private fun login() {
        val username = "admin"
        val password = "admin"
        val usernameText = composeTestRule.activity.getString(R.string.username)
        composeTestRule.onNodeWithText(usernameText).performTextInput(username)
        val passwordText = composeTestRule.activity.getString(R.string.password)
        composeTestRule.onNodeWithText(passwordText).performTextInput(password)
        val loginText = composeTestRule.activity.getString(R.string.login)
        composeTestRule.onNodeWithText(loginText).performClick()
        composeTestRule.waitUntil(
            timeoutMillis = 5000,
        ) {
            navController.currentDestination?.route == ForkTalesNavScreen.Home.route
        }
}

}