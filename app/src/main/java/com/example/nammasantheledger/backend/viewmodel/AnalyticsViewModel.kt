package com.example.nammasantheledger.backend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammasantheledger.backend.repository.AnalyticsRepository
import com.example.nammasantheledger.database.entity.AnalyticsSummary
import com.example.nammasantheledger.database.entity.CustomerEntity
import com.example.nammasantheledger.database.entity.MonthlyPendingData
import com.example.nammasantheledger.database.entity.CustomerPendingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*

/**
 * ViewModel for Analytics Screen with advanced graph support
 */
class AnalyticsViewModel(private val repository: AnalyticsRepository) : ViewModel() {

    private val _selectedMonth = MutableStateFlow(String.format(Locale.US, "%02d", Calendar.getInstance().get(Calendar.MONTH) + 1))
    val selectedMonth: StateFlow<String> = _selectedMonth

    private val _selectedYear = MutableStateFlow(Calendar.getInstance().get(Calendar.YEAR).toString())
    val selectedYear: StateFlow<String> = _selectedYear

    private val _selectedCustomerId = MutableStateFlow<Long?>(null)
    val selectedCustomerId: StateFlow<Long?> = _selectedCustomerId

    val customers: StateFlow<List<CustomerEntity>> = repository.getAllCustomers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Data for Single Customer: Month-wise trend for the selected year
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val customerTrendData: StateFlow<List<MonthlyPendingData>> = combine(
        _selectedYear, _selectedCustomerId
    ) { year, customerId ->
        year to customerId
    }.flatMapLatest { (year, customerId) ->
        if (customerId != null) {
            repository.getCustomerYearlyTrend(customerId, year)
        } else {
            flowOf(emptyList<MonthlyPendingData>())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Data for All Customers: Comparison of pending amounts for selected month
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val customerComparison: StateFlow<List<CustomerPendingData>> = combine(
        _selectedMonth, _selectedYear
    ) { month, year ->
        month to year
    }.flatMapLatest { (month, year) ->
        repository.getCustomerComparison(year, month)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val summary: StateFlow<AnalyticsSummary> = combine(
        _selectedMonth, _selectedYear, _selectedCustomerId
    ) { month, year, customerId ->
        Triple(month, year, customerId)
    }.flatMapLatest { (month, year, customerId) ->
        val balanceFlow = if (customerId == null) {
            repository.getMonthlyBalanceTotals(year, month)
        } else {
            repository.getCustomerMonthlyBalanceTotals(customerId, year, month)
        }
        
        val comparisonFlow = repository.getCustomerComparison(year, month)

        combine(balanceFlow, comparisonFlow) { balance, comparison ->
            val topCust = comparison.firstOrNull()?.customerName ?: "-"
            
            AnalyticsSummary(
                totalPending = balance.totalCredit - balance.totalPayment,
                totalCredits = balance.totalCredit,
                totalPayments = balance.totalPayment,
                topCustomer = topCust
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AnalyticsSummary())

    fun updateMonth(month: Int) {
        _selectedMonth.value = String.format(Locale.US, "%02d", month)
    }

    fun updateYear(year: String) {
        _selectedYear.value = year
    }

    fun selectCustomer(customerId: Long?) {
        _selectedCustomerId.value = customerId
    }
}
