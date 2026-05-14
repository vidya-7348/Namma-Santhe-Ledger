package com.example.nammasantheledger.database.dao

import androidx.room.*
import com.example.nammasantheledger.database.entity.TransactionEntity
import com.example.nammasantheledger.database.entity.BalanceTotals
import com.example.nammasantheledger.database.entity.MonthlyPendingData
import com.example.nammasantheledger.database.entity.CustomerPendingData
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Transaction operations
 */
@Dao
interface TransactionDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long
    
    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)
    
    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
    
    @Query("SELECT * FROM transactions WHERE customerId = :customerId ORDER BY timestamp ASC")
    fun getTransactionsByCustomerId(customerId: Long): Flow<List<TransactionEntity>>
    
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>
    
    @Query("""
        SELECT 
            COALESCE(SUM(CASE WHEN type = 'CREDIT' THEN amount ELSE 0 END), 0) as totalCredit,
            COALESCE(SUM(CASE WHEN type = 'PAYMENT' THEN amount ELSE 0 END), 0) as totalPayment
        FROM transactions
    """)
    fun getTotalBalances(): Flow<BalanceTotals>
    
    @Query("""
        SELECT COALESCE(SUM(amount), 0) 
        FROM transactions 
        WHERE type = 'CREDIT' AND timestamp >= :startOfDay
    """)
    fun getTodayCredit(startOfDay: Long): Flow<Double>
    
    @Query("""
        SELECT COALESCE(SUM(amount), 0) 
        FROM transactions 
        WHERE type = 'PAYMENT' AND timestamp >= :startOfDay
    """)
    fun getTodayPayment(startOfDay: Long): Flow<Double>
    
    @Query("""
        SELECT 
            COALESCE(
                SUM(CASE WHEN type = 'CREDIT' THEN amount ELSE -amount END),
                0
            )
        FROM transactions
    """)
    fun getTotalOutstanding(): Flow<Double>
    
    @Query("SELECT * FROM transactions WHERE timestamp >= :startDate AND timestamp <= :endDate ORDER BY timestamp ASC")
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentTransactions(limit: Int = 10): Flow<List<TransactionEntity>>

    // --- ANALYTICS QUERIES ---

    /**
     * Get daily pending totals for a specific month (Single customer daily trend)
     */
    @Query("""
        SELECT 
            strftime('%d', datetime(timestamp / 1000, 'unixepoch')) as date,
            SUM(CASE WHEN type = 'CREDIT' THEN amount ELSE -amount END) as pendingAmount
        FROM transactions
        WHERE customerId = :customerId
        AND strftime('%Y', datetime(timestamp / 1000, 'unixepoch')) = :year
        AND strftime('%m', datetime(timestamp / 1000, 'unixepoch')) = :month
        GROUP BY date
        ORDER BY date ASC
    """)
    fun getCustomerMonthlyPendingByDay(customerId: Long, year: String, month: String): Flow<List<MonthlyPendingData>>

    /**
     * Get month-wise pending trend for a specific customer in a year
     */
    @Query("""
        SELECT 
            strftime('%m', datetime(timestamp / 1000, 'unixepoch')) as date,
            SUM(CASE WHEN type = 'CREDIT' THEN amount ELSE -amount END) as pendingAmount
        FROM transactions
        WHERE customerId = :customerId
        AND strftime('%Y', datetime(timestamp / 1000, 'unixepoch')) = :year
        GROUP BY date
        ORDER BY date ASC
    """)
    fun getCustomerYearlyPendingByMonth(customerId: Long, year: String): Flow<List<MonthlyPendingData>>

    /**
     * Get customer-wise comparison for a specific month
     */
    @Query("""
        SELECT 
            c.name as customerName,
            SUM(CASE WHEN t.type = 'CREDIT' THEN t.amount ELSE -t.amount END) as pendingAmount
        FROM transactions t
        INNER JOIN customers c ON t.customerId = c.id
        WHERE strftime('%Y', datetime(t.timestamp / 1000, 'unixepoch')) = :year
        AND strftime('%m', datetime(t.timestamp / 1000, 'unixepoch')) = :month
        GROUP BY t.customerId
        HAVING pendingAmount != 0
        ORDER BY pendingAmount DESC
    """)
    fun getCustomerWisePendingComparison(year: String, month: String): Flow<List<CustomerPendingData>>

    @Query("""
        SELECT 
            COALESCE(SUM(CASE WHEN type = 'CREDIT' THEN amount ELSE 0 END), 0) as totalCredit,
            COALESCE(SUM(CASE WHEN type = 'PAYMENT' THEN amount ELSE 0 END), 0) as totalPayment
        FROM transactions
        WHERE strftime('%Y', datetime(timestamp / 1000, 'unixepoch')) = :year
        AND strftime('%m', datetime(timestamp / 1000, 'unixepoch')) = :month
    """)
    fun getMonthlyBalanceTotals(year: String, month: String): Flow<BalanceTotals>

    @Query("""
        SELECT 
            COALESCE(SUM(CASE WHEN type = 'CREDIT' THEN amount ELSE 0 END), 0) as totalCredit,
            COALESCE(SUM(CASE WHEN type = 'PAYMENT' THEN amount ELSE 0 END), 0) as totalPayment
        FROM transactions
        WHERE customerId = :customerId
        AND strftime('%Y', datetime(timestamp / 1000, 'unixepoch')) = :year
        AND strftime('%m', datetime(timestamp / 1000, 'unixepoch')) = :month
    """)
    fun getCustomerMonthlyBalanceTotals(customerId: Long, year: String, month: String): Flow<BalanceTotals>
}
