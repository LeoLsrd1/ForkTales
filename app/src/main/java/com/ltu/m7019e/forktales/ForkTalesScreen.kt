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
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
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

sealed class ForkTalesNavItem(val route: String, val icon: ImageVector, @StringRes val resourceId: Int) {
    object Home : ForkTalesNavItem("home", Icons.Default.Home, R.string.home)
    object Favorites : ForkTalesNavItem("favorites", Icons.Default.Favorite, R.string.favorites)
    object Profile : ForkTalesNavItem("profile", Icons.Default.Person, R.string.profile)
}

/**
 * A composable function that displays the bottom navigation bar for the app.
 */
@Composable
fun ForkTalesApp(
    navController: NavHostController = rememberNavController(),
) {
    val items = listOf(
        ForkTalesNavItem.Home,
        ForkTalesNavItem.Favorites,
        ForkTalesNavItem.Profile
    )
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(text = stringResource(id = screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
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
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = ForkTalesNavItem.Home.route, modifier = Modifier.padding(innerPadding)) {
            composable(ForkTalesNavItem.Home.route) { HomeScreen() }
            composable(ForkTalesNavItem.Favorites.route) { FavoritesScreen() }
            composable(ForkTalesNavItem.Profile.route) { ProfileScreen() }
        }
    }
    
}
