package com.example.nammasantheledger.backend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammasantheledger.backend.repository.CustomerRepository
import com.example.nammasantheledger.backend.repository.TransactionRepository
import com.example.nammasantheledger.database.entity.DashboardTotals
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for Home/Dashboard screen
 * Manages dashboard state and data
 */
class HomeViewModel(
    private val customerRepository: CustomerRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    
    // UI State
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadDashboardData()
    }
    
    /**
     * Load all dashboard data from repositories
     */
    private fun loadDashboardData() {
        viewModelScope.launch {
            combine(
                transactionRepository.getTotalOutstanding(),
                transactionRepository.getTodayCredit(),
                transactionRepository.getTodayPayment(),
                customerRepository.getCustomerCount()
            ) { outstanding, todayCredit, todayPayment, customerCount ->
                HomeUiState(
                    totalOutstanding = outstanding,
                    todayCredit = todayCredit,
                    todayPayment = todayPayment,
                    totalCustomers = customerCount,
                    isLoading = false
                )
            }.catch { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
    
    /**
     * Refresh dashboard data
     */
    fun refresh() {
        loadDashboardData()
    }
}

/**
 * UI State data class for Home screen
 */
data class HomeUiState(
    val totalOutstanding: Double = 0.0,
    val todayCredit: Double = 0.0,
    val todayPayment: Double = 0.0,
    val totalCustomers: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)
