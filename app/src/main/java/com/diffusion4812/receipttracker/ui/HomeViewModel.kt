package com.diffusion4812.receipttracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diffusion4812.receipttracker.data.Receipt
import com.diffusion4812.receipttracker.data.ReceiptRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel(private val receiptRepository: ReceiptRepository) : ViewModel() {
    fun getAllReceipts() : Flow<List<Receipt>> {
        return receiptRepository.getAllReceipts()
    }

    fun resetReceipts() {
        viewModelScope.launch(Dispatchers.IO) {
            receiptRepository.deleteAllReceipts()
        }
    }

    fun getClaimTotal(): Flow<Double> {
        return receiptRepository
            .getAllReceipts()
            .map { receipts ->
                receipts.sumOf { it.receiptAmount }
            }
    }
}