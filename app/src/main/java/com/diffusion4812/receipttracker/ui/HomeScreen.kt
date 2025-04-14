package com.diffusion4812.receipttracker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.diffusion4812.receipttracker.R
import com.diffusion4812.receipttracker.ReceiptTrackerTopAppBar
import com.diffusion4812.receipttracker.data.Receipt
import com.diffusion4812.receipttracker.ui.navigation.NavigationDestination
import com.diffusion4812.receipttracker.ui.theme.ReceiptTrackerTheme

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCameraPreview: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            ReceiptTrackerTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToCameraPreview() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        },
    ) { innerPadding ->
        ReceiptListScreen(
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ReceiptListScreen(
    modifier: Modifier = Modifier
) {

}

@Composable
private fun HomeBody(
    itemList: List<Receipt>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
    }
}