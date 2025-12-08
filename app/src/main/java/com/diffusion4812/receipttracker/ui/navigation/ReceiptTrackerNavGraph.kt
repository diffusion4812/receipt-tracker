package com.diffusion4812.receipttracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.diffusion4812.receipttracker.ui.AppViewModelProvider
import com.diffusion4812.receipttracker.ui.CameraPicturePreviewDestination
import com.diffusion4812.receipttracker.ui.CameraPicturePreviewScreen
import com.diffusion4812.receipttracker.ui.CameraPreviewDestination
import com.diffusion4812.receipttracker.ui.CameraPreviewScreen
import com.diffusion4812.receipttracker.ui.HomeDestination
import com.diffusion4812.receipttracker.ui.HomeScreen
import com.diffusion4812.receipttracker.data.Receipt
import kotlinx.serialization.json.Json

@Composable
fun ReceiptTrackerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        // View all receipts
        composable(route = HomeDestination.route) {
            HomeScreen(onNavigateToCameraPreviewScreen = {
                navController.navigate(
                    route = CameraPreviewDestination.route
                )
            })
        }
        // Take picture
        composable(route = CameraPreviewDestination.route) {
            CameraPreviewScreen(
                onNavigateToPicturePreviewScreen = { receiptJson ->
                    val routeWithValue = CameraPicturePreviewDestination.route
                        .replace(oldValue = "{receiptJson}", newValue = receiptJson)
                    navController.navigate(
                        route = routeWithValue
                    )
                },
                viewModel = viewModel(factory = AppViewModelProvider.Factory)
            )
        }
        // Preview and save picture as receipt
        composable(route = CameraPicturePreviewDestination.route,
            arguments = listOf(
                navArgument("receiptJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val receiptJson = backStackEntry.arguments?.getString("receiptJson")
            val receipt: Receipt = Json.decodeFromString<Receipt>(receiptJson!!)
            CameraPicturePreviewScreen(
                receipt = receipt,
                onNavigateToHomeScreen = {
                    navController.navigate(
                        route = HomeDestination.route,
                    ) {
                        popUpTo(HomeDestination.route) { inclusive = true } // Clear the back stack
                    }
                },
                onNavigateToCameraPreviewScreen = {
                    navController.navigate(
                        route = CameraPreviewDestination.route
                    )
                }
            )
        }
    }
}