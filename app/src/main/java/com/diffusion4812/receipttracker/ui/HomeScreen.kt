package com.diffusion4812.receipttracker.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.diffusion4812.receipttracker.R
import com.diffusion4812.receipttracker.ReceiptTrackerTopAppBar
import com.diffusion4812.receipttracker.data.Receipt
import com.diffusion4812.receipttracker.ui.navigation.NavigationDestination

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
            viewModel = viewModel(factory = AppViewModelProvider.Factory),
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ReceiptListScreen(
    viewModel: ReceiptViewModel,
    modifier: Modifier = Modifier
) {
    val receipts = viewModel.getAllReceipts().collectAsState(initial = emptyList())
    LazyColumn {
        items(items = receipts.value, key = { it.receiptId }) {
            ReceiptListItem(receipt = it, modifier = modifier)
        }
    }
}

@Composable
fun ReceiptListItem(
    receipt: Receipt,
    modifier: Modifier
) {
    Row {
        AsyncImage(
            model = receipt.imagePath,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .width(100.dp)
        )
    }
}