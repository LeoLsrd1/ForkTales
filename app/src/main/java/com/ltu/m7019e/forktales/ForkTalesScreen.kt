package com.ltu.m7019e.forktales

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ltu.m7019e.forktales.ui.screens.FavoritesScreen
import com.ltu.m7019e.forktales.ui.screens.HomeScreen
import com.ltu.m7019e.forktales.ui.screens.LoginScreen
import com.ltu.m7019e.forktales.ui.screens.RecipeDetailScreen
import com.ltu.m7019e.forktales.ui.screens.SearchGridScreen
import com.ltu.m7019e.forktales.viewmodel.ForkTalesViewModel
import com.ltu.m7019e.forktales.viewmodel.LoginViewModel

/**
 * A sealed class representing the different screens in the navigation of the app.
 * Each screen is represented by a route, an icon for when it's selected, an icon for when it's not selected, and a string resource id for its title.
 */
sealed class ForkTalesNavScreen(val route: String, val iconFilled: ImageVector, val iconOutlined: ImageVector, @StringRes val resourceId: Int) {
    data object Login : ForkTalesNavScreen("login", Icons.Default.Person, Icons.Outlined.Person, R.string.login)
    data object Home : ForkTalesNavScreen("home", Icons.Default.Home, Icons.Outlined.Home, R.string.home)
    data object Favorites : ForkTalesNavScreen("favorites", Icons.Default.Favorite, Icons.Outlined.FavoriteBorder, R.string.favorites)
    data object Search : ForkTalesNavScreen("search", Icons.Default.Search, Icons.Outlined.Search, R.string.search)
    data object RecipeDetail : ForkTalesNavScreen("recipeDetail", Icons.Default.MoreVert, Icons.Outlined.MoreVert, R.string.recipe_detail)
}

/**
 * A composable function that displays the bottom navigation bar/the side navigation bar for the app.
 * It uses a Scaffold to provide a consistent layout structure for the app.
 * The bottom navigation bar is created using a BottomNavigation composable, and each item in the navigation bar is a BottomNavigationItem.
 * The navigation between different screens is handled by a NavHost.
 *
 * @param windowSize The size of the window.
 * @param navController The navigation controller used to navigate between different screens.
 */
@Composable
fun ForkTalesApp(
    windowSize: WindowWidthSizeClass,
    navController: NavHostController = rememberNavController()
) {
    val forkTalesViewModel: ForkTalesViewModel = viewModel(factory = ForkTalesViewModel.Factory)
    val loginViewModel: LoginViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()

    val logo = if (isSystemInDarkTheme()) R.drawable.forktales_icon_foreground else R.drawable.forktales_icon
    val logout = R.drawable.icon_logout

    val items = listOf(
        ForkTalesNavScreen.Home,
        ForkTalesNavScreen.Favorites,
        ForkTalesNavScreen.Search
    )

    Scaffold(
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        bottomBar =
            { if (windowSize != WindowWidthSizeClass.Expanded && isLoggedIn)
                BottomAppBar(navController = navController, items = items)
            },
        topBar = { if (windowSize != WindowWidthSizeClass.Expanded && isLoggedIn
            && currentDestination?.route == ForkTalesNavScreen.Home.route)
                TopAppBar(
                    loginViewModel = loginViewModel,
                    logo = logo,
                    logout = logout
                )
            }
    ) { innerPadding ->
        Row {
            if (windowSize == WindowWidthSizeClass.Expanded && isLoggedIn) {
                SideAppBar(
                    navController = navController,
                    loginViewModel = loginViewModel,
                    logo = logo,
                    logout = logout,
                    items = items,
                    currentDestination = currentDestination
                )
            }
            NavGraphScheme(
                forkTalesViewModel = forkTalesViewModel,
                loginViewModel = loginViewModel,
                navController = navController,
                innerPadding = innerPadding,
                windowSize = windowSize
            )
        }
    }
}

/**
 * A Composable function that represents the side navigation bar for expanded windows.
 *
 * @param navController The navigation controller used to navigate between different screens.
 * @param loginViewModel The Login View Model.
 * @param logo The ID of the ForkTales logo.
 * @param logout The ID of the logout icon.
 * @param items The different screens.
 * @param currentDestination The current navigation destination.
 */
@Composable
fun SideAppBar(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    logo: Int,
    logout: Int,
    items: List<ForkTalesNavScreen>,
    currentDestination: NavDestination?
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(200.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(logo),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                loginViewModel.onLogoutClick()
            }) {
                Icon(
                    painterResource(logout),
                    modifier = Modifier.size(24.dp),
                    contentDescription = null
                )
            }
        }
        items.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = if (isSelected) screen.iconFilled else screen.iconOutlined,
                        contentDescription = stringResource(id = screen.resourceId),
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = screen.resourceId),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

/**
 * A Composable function that represents the top navigation bar for not expanded windows.
 *
 * @param loginViewModel The Login View Model.
 * @param logo The ID of the ForkTales logo.
 * @param logout The ID of the logout icon.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    loginViewModel: LoginViewModel,
    logo: Int,
    logout: Int
) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(logo),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    loginViewModel.onLogoutClick()
                }) {
                    Icon(
                        painterResource(logout),
                        modifier = Modifier.size(24.dp),
                        contentDescription = null)
                }
            }
        }
    )
}

/**
 * A Composable function that represents the bottom navigation bar for compact and medium windows.
 *
 * @param navController The navigation controller used to navigate between different screens.
 * @param items The different screens.
 */
@Composable
fun BottomAppBar(
    navController: NavHostController,
    items: List<ForkTalesNavScreen>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
    ) {
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            BottomNavigationItem(
                icon = {
                    Icon(
                        if (isSelected) screen.iconFilled else screen.iconOutlined,
                        contentDescription = stringResource(id = screen.resourceId)
                    )
                },
                label = { Text(text = stringResource(id = screen.resourceId)) },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}


/**
 * A composable function that describes the different screens to display according to the route.
 *
 * @param forkTalesViewModel The View Model.
 * @param loginViewModel The Login View Model.
 * @param navController The navigation controller used to navigate between different screens.
 * @param innerPadding The padding.
 * @param windowSize The size of the window.
 */
@Composable
fun NavGraphScheme(
    forkTalesViewModel: ForkTalesViewModel,
    loginViewModel: LoginViewModel,
    navController: NavHostController,
    innerPadding: PaddingValues,
    windowSize: WindowWidthSizeClass
) {
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()
    NavHost(
        navController = navController,
        startDestination = ForkTalesNavScreen.Login.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(ForkTalesNavScreen.Login.route) {
            LoginScreen(
                loginViewModel = loginViewModel,
                navController = navController
            )
        }
        composable(ForkTalesNavScreen.Home.route) {
            if (isLoggedIn) {
                HomeScreen(
                    recipeListUiState = forkTalesViewModel.recipeListUiState,
                    categoriesFlow = forkTalesViewModel.categories,
                    onRecipeListItemClicked = {
                        forkTalesViewModel.selectedRecipeName = it.strMeal
                        forkTalesViewModel.getRecipeDetailsById(it.idMeal)
                        navController.navigate(ForkTalesNavScreen.RecipeDetail.route)
                    }
                )
            } else {
                navController.navigate(ForkTalesNavScreen.Login.route)
            }
        }
        composable(ForkTalesNavScreen.Search.route) {
            if (isLoggedIn) {
                SearchGridScreen(
                    viewModel = forkTalesViewModel,
                    onRecipeListItemClicked = {
                        forkTalesViewModel.getRecipeDetailsByObject(it)
                        navController.navigate(ForkTalesNavScreen.RecipeDetail.route)
                    },
                    windowSize = windowSize
                )
            } else {
                navController.navigate(ForkTalesNavScreen.Login.route)
            }
        }
            composable(ForkTalesNavScreen.Favorites.route) {
                if (isLoggedIn) {
                    FavoritesScreen(
                        favoriteRecipesListUiState = forkTalesViewModel.favoriteRecipesListUiState,
                        categoriesFlow = forkTalesViewModel.categories,
                        onRecipeListItemClicked = {
                            forkTalesViewModel.selectedRecipeName = it.strMeal
                            forkTalesViewModel.getRecipeDetailsById(it.idMeal)
                            navController.navigate(ForkTalesNavScreen.RecipeDetail.route)
                        }
                    )
                } else {
                    navController.navigate(ForkTalesNavScreen.Login.route)
                }
            }
            composable(ForkTalesNavScreen.RecipeDetail.route) {
                if(isLoggedIn) {
                    RecipeDetailScreen(
                        forkTalesViewModel = forkTalesViewModel,
                        recipeName = forkTalesViewModel.selectedRecipeName,
                        navigateUp = { navController.navigateUp() },
                        windowSize = windowSize
                    )
                } else {
                    navController.navigate(ForkTalesNavScreen.Login.route)
            }
        }
    }
}