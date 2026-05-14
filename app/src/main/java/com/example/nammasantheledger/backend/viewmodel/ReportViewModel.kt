package com.example.nammasantheledger.backend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammasantheledger.backend.repository.TransactionRepository
import com.example.nammasantheledger.backend.utils.DateUtils
import com.example.nammasantheledger.database.entity.TransactionEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for Reports screen
 * Manages report data and date range filtering
 */
class ReportViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    
    // UI State
    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()
    
    init {
        loadTodayReport()
    }
    
    /**
     * Load today's report
     */
    fun loadTodayReport() {
        viewModelScope.launch {
            val startDate = DateUtils.getStartOfDay()
            val endDate = DateUtils.getEndOfDay()
            
            transactionRepository.getTransactionsByDateRange(startDate, endDate)
                .collect { transactions ->
                    val (totalCredit, totalPayment) = calculateTotals(transactions)
                    _uiState.value = ReportUiState(
                        transactions = transactions,
                        totalCredit = totalCredit,
                        totalPayment = totalPayment,
                        period = "Today",
                        isLoading = false
                    )
                }
        }
    }
    
    /**
     * Load weekly report
     */
    fun loadWeeklyReport() {
        viewModelScope.launch {
            val startDate = DateUtils.getStartOfWeek()
            val endDate = DateUtils.getEndOfDay()
            
            transactionRepository.getTransactionsByDateRange(startDate, endDate)
                .collect { transactions ->
                    val (totalCredit, totalPayment) = calculateTotals(transactions)
                    _uiState.value = ReportUiState(
                        transactions = transactions,
                        totalCredit = totalCredit,
                        totalPayment = totalPayment,
                        period = "This Week",
                        isLoading = false
                    )
                }
        }
    }
    
    /**
     * Load monthly report
     */
    fun loadMonthlyReport() {
        viewModelScope.launch {
            val startDate = DateUtils.getStartOfMonth()
            val endDate = DateUtils.getEndOfDay()
            
            transactionRepository.getTransactionsByDateRange(startDate, endDate)
                .collect { transactions ->
                    val (totalCredit, totalPayment) = calculateTotals(transactions)
                    _uiState.value = ReportUiState(
                        transactions = transactions,
                        totalCredit = totalCredit,
                        totalPayment = totalPayment,
                        period = "This Month",
                        isLoading = false
                    )
                }
        }
    }
    
    /**
     * Calculate total credit and payment from transactions
     */
    private fun calculateTotals(transactions: List<TransactionEntity>): Pair<Double, Double> {
        var totalCredit = 0.0
        var totalPayment = 0.0
        
        transactions.forEach { transaction ->
            when (transaction.type) {
                com.example.nammasantheledger.database.entity.TransactionType.CREDIT -> 
                    totalCredit += transaction.amount
                com.example.nammasantheledger.database.entity.TransactionType.PAYMENT -> 
                    totalPayment += transaction.amount
            }
        }
        
        return Pair(totalCredit, totalPayment)
    }
    
    /**
     * Refresh current report
     */
    fun refresh() {
        when (_uiState.value.period) {
            "Today" -> loadTodayReport()
            "This Week" -> loadWeeklyReport()
            "This Month" -> loadMonthlyReport()
            else -> loadTodayReport()
        }
    }
}

/**
 * UI State data class for Reports screen
 */
data class ReportUiState(
    val transactions: List<TransactionEntity> = emptyList(),
    val totalCredit: Double = 0.0,
    val totalPayment: Double = 0.0,
    val period: String = "Today",
    val isLoading: Boolean = true,
    val error: String? = null
) {
    val netBalance: Double get() = totalCredit - totalPayment
}
