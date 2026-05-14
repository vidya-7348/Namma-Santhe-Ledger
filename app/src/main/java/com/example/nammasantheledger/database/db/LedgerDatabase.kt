package com.example.nammasantheledger.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.nammasantheledger.database.dao.CustomerDao
import com.example.nammasantheledger.database.dao.TransactionDao
import com.example.nammasantheledger.database.entity.CustomerEntity
import com.example.nammasantheledger.database.entity.TransactionEntity
import com.example.nammasantheledger.database.utils.Converters

/**
 * Room Database class for the Namma-Santhe Ledger application
 * Singleton pattern to ensure only one instance exists
 */
@Database(
    entities = [CustomerEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LedgerDatabase : RoomDatabase() {
    
    /**
     * Abstract method to get Customer DAO
     */
    abstract fun customerDao(): CustomerDao
    
    /**
     * Abstract method to get Transaction DAO
     */
    abstract fun transactionDao(): TransactionDao
    
    companion object {
        @Volatile
        private var INSTANCE: LedgerDatabase? = null
        
        /**
         * Get singleton instance of the database
         * Uses double-checked locking for thread safety
         */
        fun getInstance(context: Context): LedgerDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }
        
        /**
         * Build the database instance
         */
        private fun buildDatabase(context: Context): LedgerDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                LedgerDatabase::class.java,
                "namma_santhe_ledger.db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
