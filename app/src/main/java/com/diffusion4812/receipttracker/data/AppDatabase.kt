package com.diffusion4812.receipttracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExpenseClaim::class, Receipt::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun expenseClaimDao(): ExpenseClaimDao
    abstract fun receiptDao(): ReceiptDao

    fun getExpenseClaimRepository() : ExpenseClaimRepository {
        return ExpenseClaimRepositoryImpl(expenseClaimDao())
    }

    fun getReceiptRepository() : ReceiptRepository {
        return ReceiptRepositoryImpl(receiptDao())
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}