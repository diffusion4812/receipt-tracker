package com.diffusion4812.receipttracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(receipt: Receipt)

    @Update
    suspend fun update(receipt: Receipt)

    @Delete
    suspend fun delete(receipt: Receipt)

    @Query("SELECT * FROM receipts WHERE claimId = :claimId")
    fun getReceiptsForClaim(claimId: Int): Flow<List<Receipt>>

    @Query("SELECT * FROM receipts WHERE receiptId = :receiptId")
    fun getReceiptById(receiptId: Int): Flow<Receipt>

    @Query("SELECT * FROM receipts")
    fun getAllReceipts(): Flow<List<Receipt>>
}