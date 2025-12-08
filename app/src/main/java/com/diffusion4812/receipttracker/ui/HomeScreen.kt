package com.diffusion4812.receipttracker.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
    onNavigateToCameraPreviewScreen: () -> Unit,
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
                onClick = { onNavigateToCameraPreviewScreen() }) {
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
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val receipts = viewModel.getAllReceipts().collectAsState(initial = emptyList())
    LazyColumn(modifier) {
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image Preview
        AsyncImage(
            model = receipt.imagePath,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Text Column (Description + Amount)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = receipt.receiptDescription,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Amount: \$${receipt.receiptAmount}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }
    }
}