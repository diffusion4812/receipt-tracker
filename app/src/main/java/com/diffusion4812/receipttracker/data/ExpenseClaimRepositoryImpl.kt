package com.diffusion4812.receipttracker.data

import kotlinx.coroutines.flow.Flow

class ExpenseClaimRepositoryImpl(private val expenseClaimDao: ExpenseClaimDao) : ExpenseClaimRepository {
    override suspend fun insert(expenseClaim: ExpenseClaim) {
        expenseClaimDao.insert(expenseClaim)
    }

    override suspend fun update(expenseClaim: ExpenseClaim) {
        expenseClaimDao.update(expenseClaim)
    }

    override suspend fun delete(expenseClaim: ExpenseClaim) {
        expenseClaimDao.delete(expenseClaim)
    }

    override fun getAllClaims(): Flow<List<ExpenseClaim>> {
        return expenseClaimDao.getAllClaims()
    }

    override fun getClaimById(claimId: Int): Flow<ExpenseClaim> {
        return expenseClaimDao.getClaimById(claimId)
    }

    override fun getSubmittedClaims(): Flow<List<ExpenseClaim>> {
        return expenseClaimDao.getSubmittedClaims()
    }

    override fun getPendingClaims(): Flow<List<ExpenseClaim>> {
        return expenseClaimDao.getPendingClaims()
    }
}