package com.diffusion4812.receipttracker.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.diffusion4812.receipttracker.ReceiptTrackerApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            CameraPreviewViewModel(
                receiptTrackerApplication().container.receiptRepository
            )
        }
        initializer {
            ReceiptViewModel(
                receiptTrackerApplication().container.receiptRepository
            )
        }
    }
}

fun CreationExtras.receiptTrackerApplication(): ReceiptTrackerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ReceiptTrackerApplication)