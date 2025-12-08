package com.diffusion4812.receipttracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diffusion4812.receipttracker.data.Receipt
import com.diffusion4812.receipttracker.data.ReceiptRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(private val receiptRepository: ReceiptRepository) : ViewModel() {
    fun getAllReceipts() : Flow<List<Receipt>> {
        return receiptRepository.getAllReceipts()
    }
}