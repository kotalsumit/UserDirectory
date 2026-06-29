package com.sumit.userdirectory.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sumit.userdirectory.feature.users.presentation.detail.UserDetailScreen
import com.sumit.userdirectory.feature.users.presentation.list.UserListScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.USERS,
    ) {
        composable(AppRoutes.USERS) {
            UserListScreen(
                onUserClick = { userId ->
                    navController.navigate(AppRoutes.userDetail(userId))
                },
            )
        }
        composable(
            route = AppRoutes.USER_DETAIL,
            arguments = listOf(
                navArgument(AppRoutes.USER_ID_ARGUMENT) {
                    type = NavType.IntType
                },
            ),
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt(AppRoutes.USER_ID_ARGUMENT) ?: return@composable

            UserDetailScreen(
                userId = userId,
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}
