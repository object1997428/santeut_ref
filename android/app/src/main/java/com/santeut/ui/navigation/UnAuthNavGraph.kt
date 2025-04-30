package com.santeut.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.santeut.ui.landing.LandingScreen
import com.santeut.ui.login.LoginScreen
import com.santeut.ui.signup.SignUpScreen

fun NavGraphBuilder.UnAuthNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "Landing",
        route = "auth_graph"
    ) {
        composable("landing") {
            LandingScreen(
                onNavigateLogin = {
                    navController.navigate(route = "login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateHome = {
                    navController.navigate(route = "home_graph") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("login") {
            LoginScreen(
                onNavigateSignUp = {
                    navController.navigate(route = "signup")
                },
                onNavigateHome = {
                    navController.navigate(route = "home_graph") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("signup") {
            SignUpScreen(
                onNavigateLogin = {
                    navController.navigate(route = "login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}