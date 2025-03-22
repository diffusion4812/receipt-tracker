package com.diffusion4812.receipttracker.data

import kotlinx.coroutines.flow.Flow

class ReceiptRepositoryImpl(private val receiptDao: ReceiptDao) : ReceiptRepository {
    override suspend fun insert(receipt: Receipt) {
        receiptDao.insert(receipt)
    }

    override suspend fun update(receipt: Receipt) {
        receiptDao.update(receipt)
    }

    override suspend fun delete(receipt: Receipt) {
        receiptDao.delete(receipt)
    }

    override fun getReceiptsForClaim(claimId: Int): Flow<List<Receipt>> {
        return receiptDao.getReceiptsForClaim(claimId)
    }

    override fun getReceiptById(receiptId: Int): Flow<Receipt> {
        return receiptDao.getReceiptById(receiptId)
    }
    override fun getAllReceipts(): Flow<List<Receipt>> {
        return receiptDao.getAllReceipts()
    }
}