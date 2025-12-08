package com.diffusion4812.receipttracker.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "receipts",
    foreignKeys = [ForeignKey(
        entity = Receipt::class,
        parentColumns = ["receiptId"],
        childColumns = ["receiptId"],
        onDelete = ForeignKey.CASCADE // This will delete the receipt if the claim is deleted
    )]
)

@Serializable
data class Receipt(
    @PrimaryKey(autoGenerate = true) val receiptId: Int = 0,
    val claimId: Int,
    val receiptDate: Long,
    val receiptDescription: String,
    val receiptAmount: Double,
    val imagePath: String
)