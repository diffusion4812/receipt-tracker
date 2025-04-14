package com.diffusion4812.receipttracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.diffusion4812.receipttracker.ui.CameraPreviewDestination
import com.diffusion4812.receipttracker.ui.CameraPreviewScreen
import com.diffusion4812.receipttracker.ui.HomeDestination
import com.diffusion4812.receipttracker.ui.HomeScreen

@Composable
fun ReceiptTrackerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController, startDestination = HomeDestination.route, modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(onNavigateToCameraPreview = {
                navController.navigate(
                    route = CameraPreviewDestination.route
                )
            })
        }
        composable(route = CameraPreviewDestination.route) {
            CameraPreviewScreen()
        }
    }
}