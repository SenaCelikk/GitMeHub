package com.senacelik.gitmehub.navigation

import android.R.attr.type
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.senacelik.gitmehub.presentation.repolist.RepoListViewModel
import com.senacelik.gitmehub.screens.RepoListScreen
import androidx.navigation.navArgument

import com.senacelik.gitmehub.presentation.starred.StarredViewModel
import com.senacelik.gitmehub.screens.RepoDetailScreen
import com.senacelik.gitmehub.screens.StarredScreen

@Composable
fun GitMeHubNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.RepoList.route
    ) {
        composable(route = Screen.RepoList.route) {
            val viewModel: RepoListViewModel = hiltViewModel()
            RepoListScreen(
                viewModel = viewModel,
                onNavigateToDetail = { url ->
                    // We encode the URL because it contains '/' which breaks routes
                    navController.navigate(Screen.Detail.passUrl(url))
                }
            )
        }

        composable(route = Screen.Starred.route) {
            val viewModel: StarredViewModel = hiltViewModel()
            StarredScreen(
                viewModel = viewModel,
                onNavigateToDetail = { url ->
                    navController.navigate(Screen.Detail.passUrl(url))
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("url") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Extract the URL and show the Detail Screen
            val url = backStackEntry.arguments?.getString("url") ?: ""
            RepoDetailScreen(
                url = url,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
