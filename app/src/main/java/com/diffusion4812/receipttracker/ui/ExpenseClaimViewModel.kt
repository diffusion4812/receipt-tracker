package com.diffusion4812.receipttracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diffusion4812.receipttracker.data.ExpenseClaim
import com.diffusion4812.receipttracker.data.ExpenseClaimRepository
import kotlinx.coroutines.launch

class ExpenseClaimViewModel(private val expenseClaimRepository: ExpenseClaimRepository) : ViewModel() {

    fun addNewClaim(expenseClaim: ExpenseClaim) {
        viewModelScope.launch {
            expenseClaimRepository.insert(expenseClaim)
        }
    }
}