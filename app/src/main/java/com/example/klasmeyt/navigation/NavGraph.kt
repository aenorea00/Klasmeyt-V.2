package com.example.klasmeyt.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.klasmeyt.ui.screens.CreateEditPostScreen
import com.example.klasmeyt.ui.screens.FeedScreen
import com.example.klasmeyt.ui.screens.LoginScreen
import com.example.klasmeyt.ui.screens.PostDetailScreen
import com.example.klasmeyt.ui.screens.ProfileScreen
import com.example.klasmeyt.ui.screens.RegisterScreen
import com.example.klasmeyt.viewmodel.FeedViewModel

@Composable
fun AppNavGraph(
    feedViewModel: FeedViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { user ->
                    feedViewModel.updateCurrentUser(user)
                    navController.navigate(Screen.Feed.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable(route = "register") {
            RegisterScreen(
                onRegistrationSuccess = {
                    navController.popBackStack()
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.Feed.route) {
            FeedScreen(
                viewModel = feedViewModel,
                onNavigateToCreate = {
                    navController.navigate(Screen.CreatePost.route)
                },
                onNavigateToDetail = { postId ->
                    navController.navigate(Screen.PostDetail.createRoute(postId))
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                },
                onNavigateToUserProfile = { studentNumber ->
                    navController.navigate("profile?studentNumber=$studentNumber")
                }
            )
        }

        composable(
            route = "profile?studentNumber={studentNumber}",
            arguments = listOf(navArgument("studentNumber") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val targetStudentNumber = backStackEntry.arguments?.getString("studentNumber")
            ProfileScreen(
                targetStudentNumber = targetStudentNumber,
                viewModel = feedViewModel,
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    feedViewModel.updateCurrentUser(null)
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.PostDetail.route,
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            PostDetailScreen(
                postId = postId,
                viewModel = feedViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id ->
                    navController.navigate(Screen.EditPost.createRoute(id))
                }
            )
        }

        composable(route = Screen.CreatePost.route) {
            CreateEditPostScreen(
                postId = null,
                viewModel = feedViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditPost.route,
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId")
            CreateEditPostScreen(
                postId = postId,
                viewModel = feedViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}