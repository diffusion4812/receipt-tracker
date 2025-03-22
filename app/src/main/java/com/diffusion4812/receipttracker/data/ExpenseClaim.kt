package com.diffusion4812.receipttracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_claims")
data class ExpenseClaim(
    @PrimaryKey(autoGenerate = true) val claimId: Long = 0,
    val claimDate: Long,
    val claimDescription: String,
    var isSubmitted: Boolean = false
)