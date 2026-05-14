package com.example.nammasantheledger.backend.repository

import com.example.nammasantheledger.database.dao.TransactionDao
import com.example.nammasantheledger.database.dao.CustomerDao
import com.example.nammasantheledger.database.entity.MonthlyPendingData
import com.example.nammasantheledger.database.entity.CustomerPendingData
import com.example.nammasantheledger.database.entity.BalanceTotals
import com.example.nammasantheledger.database.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository for Analytics related data operations
 */
class AnalyticsRepository(
    private val transactionDao: TransactionDao,
    private val customerDao: CustomerDao
) {
    /**
     * Get all customers for filtering
     */
    fun getAllCustomers(): Flow<List<CustomerEntity>> = customerDao.getAllCustomers()

    /**
     * Get customer-wise comparison for a specific month
     */
    fun getCustomerComparison(year: String, month: String): Flow<List<CustomerPendingData>> {
        return transactionDao.getCustomerWisePendingComparison(year, month)
    }

    /**
     * Get yearly trend (Month-wise) for a specific customer
     */
    fun getCustomerYearlyTrend(customerId: Long, year: String): Flow<List<MonthlyPendingData>> {
        return transactionDao.getCustomerYearlyPendingByMonth(customerId, year)
    }

    /**
     * Get monthly balance totals for all customers
     */
    fun getMonthlyBalanceTotals(year: String, month: String): Flow<BalanceTotals> {
        return transactionDao.getMonthlyBalanceTotals(year, month)
    }

    /**
     * Get monthly balance totals for a specific customer
     */
    fun getCustomerMonthlyBalanceTotals(customerId: Long, year: String, month: String): Flow<BalanceTotals> {
        return transactionDao.getCustomerMonthlyBalanceTotals(customerId, year, month)
    }
}
