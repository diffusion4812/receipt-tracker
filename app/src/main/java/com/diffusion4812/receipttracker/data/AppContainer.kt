package com.diffusion4812.receipttracker.data

import android.content.Context

interface AppContainer {
    val expenseClaimRepository: ExpenseClaimRepository
    val receiptRepository: ReceiptRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val expenseClaimRepository: ExpenseClaimRepository by lazy {
        ExpenseClaimRepositoryImpl(AppDatabase.getDatabase(context).expenseClaimDao())
    }

    override val receiptRepository: ReceiptRepository by lazy {
        ReceiptRepositoryImpl(AppDatabase.getDatabase(context).receiptDao())
    }
}