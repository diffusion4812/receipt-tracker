package com.diffusion4812.receipttracker

import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.diffusion4812.receipttracker.ui.navigation.ReceiptTrackerNavHost

/**
 * Top level composable that represents screens for the application.
 */
@Composable
fun ReceiptTrackerApp(navController: NavHostController = rememberNavController()) {
    ReceiptTrackerNavHost(
        navController = navController
    )
}

/**
 * App bar to display title and conditionally display the back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptTrackerTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        })
}