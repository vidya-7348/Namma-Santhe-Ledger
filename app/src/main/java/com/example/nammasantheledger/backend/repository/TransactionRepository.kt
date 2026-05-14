package com.example.nammasantheledger.backend.repository

import com.example.nammasantheledger.database.dao.TransactionDao
import com.example.nammasantheledger.database.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

/**
 * Repository class for Transaction operations
 * Acts as a single source of truth for transaction data
 */
class TransactionRepository(
    private val transactionDao: TransactionDao
) {
    
    /**
     * Get all transactions for a specific customer
     */
    fun getTransactionsByCustomerId(customerId: Long): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsByCustomerId(customerId)
    }
    
    /**
     * Get all transactions
     */
    fun getAllTransactions(): Flow<List<TransactionEntity>> {
        return transactionDao.getAllTransactions()
    }
    
    /**
     * Insert a new transaction
     */
    suspend fun insertTransaction(transaction: TransactionEntity): Long {
        return transactionDao.insertTransaction(transaction)
    }
    
    /**
     * Update an existing transaction
     */
    suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.updateTransaction(transaction)
    }
    
    /**
     * Delete a transaction
     */
    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
    }
    
    /**
     * Get today's credit total
     */
    fun getTodayCredit(): Flow<Double> {
        val startOfDay = getStartOfDay()
        return transactionDao.getTodayCredit(startOfDay)
    }
    
    /**
     * Get today's payment total
     */
    fun getTodayPayment(): Flow<Double> {
        val startOfDay = getStartOfDay()
        return transactionDao.getTodayPayment(startOfDay)
    }
    
    /**
     * Get total outstanding balance
     */
    fun getTotalOutstanding(): Flow<Double> {
        return transactionDao.getTotalOutstanding()
    }
    
    /**
     * Get transactions within a date range
     */
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsByDateRange(startDate, endDate)
    }
    
    /**
     * Get recent transactions
     */
    fun getRecentTransactions(limit: Int = 10): Flow<List<TransactionEntity>> {
        return transactionDao.getRecentTransactions(limit)
    }
    
    /**
     * Get start of current day (midnight)
     */
    private fun getStartOfDay(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
