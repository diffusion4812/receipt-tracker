package com.diffusion4812.receipttracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diffusion4812.receipttracker.data.Receipt
import com.diffusion4812.receipttracker.data.ReceiptRepository
import kotlinx.coroutines.launch

class ReceiptViewModel(private val receiptRepository: ReceiptRepository) : ViewModel() {

    fun addReceipt(receipt: Receipt) {
        viewModelScope.launch {
            receiptRepository.insert(receipt)
        }
    }
}