package com.diffusion4812.receipttracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.diffusion4812.receipttracker.R
import com.diffusion4812.receipttracker.data.Receipt
import com.diffusion4812.receipttracker.ui.navigation.NavigationDestination

object CameraPicturePreviewDestination : NavigationDestination {
    override val route = "camera_picture_preview/{receiptJson}"
    override val titleRes = R.string.app_name
}

@Composable
fun CameraPicturePreviewScreen(
    viewModel: PicturePreviewAndSaveViewModel = viewModel(factory = AppViewModelProvider.Factory),
    receipt: Receipt,
    onNavigateToHomeScreen: () -> Unit,
    onNavigateToCameraPreviewScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    var receiptDescription by remember { mutableStateOf("") }
    var receiptAmount by remember { mutableStateOf("") }
    Scaffold(
        floatingActionButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                FloatingActionButton(
                    onClick = {
                        onNavigateToCameraPreviewScreen() // Take a new picture
                    }
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "")
                }
                FloatingActionButton(
                    onClick = {
                        viewModel.addReceipt(
                            Receipt(
                                claimId = 0,
                                receiptDate = System.currentTimeMillis(),
                                receiptDescription = receiptDescription,
                                receiptAmount = receiptAmount.toDoubleOrNull() ?: 0.0,
                                imagePath = receipt.imagePath
                            )
                        )
                        onNavigateToHomeScreen() // Receipt saved, navigate home
                    },
                ) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = "")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image at the top, centered
            AsyncImage(
                model = receipt.imagePath,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Adjust height as needed
            )

            Spacer(modifier = Modifier.height(16.dp)) // Space between image and fields

            // Description field
            TextField(
                value = receiptDescription,
                label = { Text(text = "Description") },
                onValueChange = { newValue -> receiptDescription = newValue },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp)) // Space between fields

            // Amount field
            TextField(
                value = receiptAmount,
                label = { Text(text = "Amount") },
                onValueChange = { newValue -> receiptAmount = newValue },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CameraPicturePreviewScreenPreview(
    modifier: Modifier = Modifier
) {
    val receipt: Receipt = Receipt(
        claimId = 0,
        receiptDate = System.currentTimeMillis(),
        receiptDescription = "Receipt",
        receiptAmount = 0.0,
        imagePath = ""
    )
    CameraPicturePreviewScreen(receipt = receipt,
        onNavigateToHomeScreen = { },
        onNavigateToCameraPreviewScreen = { }
    )
}

