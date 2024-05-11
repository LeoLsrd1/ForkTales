package com.ltu.m7019e.forktales

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ltu.m7019e.forktales.ui.screens.FavoritesScreen
import com.ltu.m7019e.forktales.ui.screens.HomeScreen
import com.ltu.m7019e.forktales.ui.screens.ProfileScreen
import com.ltu.m7019e.forktales.ui.screens.RecipeDetailScreen
import com.ltu.m7019e.forktales.viewmodel.ForkTalesViewModel

sealed class ForkTalesNavScreen(val route: String, val iconFilled: ImageVector, val iconOutlined: ImageVector, @StringRes val resourceId: Int) {
    data object Home : ForkTalesNavScreen("home", Icons.Default.Home, Icons.Outlined.Home, R.string.home)
    data object Favorites : ForkTalesNavScreen("favorites", Icons.Default.Favorite, Icons.Outlined.FavoriteBorder, R.string.favorites)
    data object Search : ForkTalesNavScreen("search", Icons.Default.Search, Icons.Outlined.Search, R.string.search)
    data object RecipeDetail : ForkTalesNavScreen("recipeDetail", Icons.Default.MoreVert, Icons.Outlined.MoreVert, R.string.recipe_detail)
}

/**
 * A composable function that displays the bottom navigation bar for the app.
 */
@Composable
fun ForkTalesApp(
    navController: NavHostController = rememberNavController(),
) {
    val forkTalesViewModel: ForkTalesViewModel = viewModel()
    val items = listOf(
        ForkTalesNavScreen.Home,
        ForkTalesNavScreen.Favorites,
        ForkTalesNavScreen.Search
    )
    Scaffold(
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        bottomBar = {
            BottomNavigation(
                backgroundColor = MaterialTheme.colorScheme.background,
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                if (isSelected) screen.iconFilled else screen.iconOutlined,
                                contentDescription = null
                            )
                               },
                        label = { Text(text = stringResource(id = screen.resourceId)) },
                        selected = isSelected,
                        onClick = {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = ForkTalesNavScreen.Home.route, modifier = Modifier.padding(innerPadding)) {
            composable(ForkTalesNavScreen.Home.route) {
                HomeScreen(
                    onRecipeListItemClicked = {
                        forkTalesViewModel.selectedRecipe = it
                        navController.navigate(ForkTalesNavScreen.RecipeDetail.route)
                    }
                )
            }
            composable(ForkTalesNavScreen.Search.route) { ProfileScreen() }
            composable(ForkTalesNavScreen.Favorites.route) { FavoritesScreen() }
            composable(ForkTalesNavScreen.RecipeDetail.route) {
                RecipeDetailScreen(
                    forkTalesViewModel = forkTalesViewModel,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
    
}
