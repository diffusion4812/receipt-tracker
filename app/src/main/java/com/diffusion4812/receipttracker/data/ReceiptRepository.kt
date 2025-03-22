package com.diffusion4812.receipttracker.data

import kotlinx.coroutines.flow.Flow

interface ReceiptRepository {
    suspend fun insert(receipt: Receipt)
    suspend fun update(receipt: Receipt)
    suspend fun delete(receipt: Receipt)
    fun getReceiptsForClaim(claimId: Int): Flow<List<Receipt>>
    fun getReceiptById(receiptId: Int): Flow<Receipt>
    fun getAllReceipts(): Flow<List<Receipt>>
}