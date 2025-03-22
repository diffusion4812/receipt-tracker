package com.diffusion4812.receipttracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseClaimDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expenseClaim: ExpenseClaim)

    @Update
    suspend fun update(expenseClaim: ExpenseClaim)

    @Delete
    suspend fun delete(expenseClaim: ExpenseClaim)

    @Query("SELECT * FROM expense_claims")
    fun getAllClaims(): Flow<List<ExpenseClaim>>

    @Query("SELECT * FROM expense_claims WHERE claimId = :claimId")
    fun getClaimById(claimId: Int): Flow<ExpenseClaim>

    @Query("SELECT * FROM expense_claims WHERE isSubmitted = 1")
    fun getSubmittedClaims(): Flow<List<ExpenseClaim>>

    @Query("SELECT * FROM expense_claims WHERE isSubmitted = 0")
    fun getPendingClaims(): Flow<List<ExpenseClaim>>
}