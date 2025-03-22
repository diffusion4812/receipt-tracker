package com.diffusion4812.receipttracker.data

import kotlinx.coroutines.flow.Flow

interface ExpenseClaimRepository {
    suspend fun insert(expenseClaim: ExpenseClaim)
    suspend fun update(expenseClaim: ExpenseClaim)
    suspend fun delete(expenseClaim: ExpenseClaim)
    fun getAllClaims(): Flow<List<ExpenseClaim>>
    fun getClaimById(claimId: Int): Flow<ExpenseClaim>
    fun getSubmittedClaims(): Flow<List<ExpenseClaim>>
    fun getPendingClaims(): Flow<List<ExpenseClaim>>
}