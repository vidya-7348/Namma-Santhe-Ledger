package com.example.nammasantheledger.backend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammasantheledger.backend.repository.CustomerRepository
import com.example.nammasantheledger.backend.repository.TransactionRepository
import com.example.nammasantheledger.backend.usecase.CalculateBalanceUseCase
import com.example.nammasantheledger.database.entity.CustomerEntity
import com.example.nammasantheledger.database.entity.TransactionEntity
import com.example.nammasantheledger.database.entity.TransactionType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for Transaction screens
 * Manages transaction operations and ledger state
 */
class TransactionViewModel(
    private val transactionRepository: TransactionRepository,
    private val customerRepository: CustomerRepository
) : ViewModel() {
    
    // UI State
    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()
    
    // Available customers for transaction
    private val _customers = MutableStateFlow<List<CustomerEntity>>(emptyList())
    val customers: StateFlow<List<CustomerEntity>> = _customers.asStateFlow()
    
    // Snackbar message
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()
    
    init {
        loadCustomers()
    }
    
    /**
     * Load all customers for dropdown
     */
    private fun loadCustomers() {
        viewModelScope.launch {
            customerRepository.getAllCustomers()
                .collect { customers ->
                    _customers.value = customers
                }
        }
    }
    
    /**
     * Load transactions for a specific customer (ledger)
     */
    fun loadCustomerLedger(customerId: Long) {
        viewModelScope.launch {
            combine(
                transactionRepository.getTransactionsByCustomerId(customerId),
                customerRepository.getCustomerById(customerId)
            ) { transactions, customer ->
                val balanceData = CalculateBalanceUseCase()(transactions)
                
                TransactionUiState(
                    transactions = transactions,
                    customer = customer,
                    totalCredit = balanceData.totalCredit,
                    totalPayment = balanceData.totalPayment,
                    pendingBalance = balanceData.pendingBalance,
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
     * Add new transaction (credit or payment)
     */
    fun addTransaction(
        customerId: Long,
        amount: Double,
        type: TransactionType,
        notes: String?
    ) {
        viewModelScope.launch {
            try {
                val transaction = TransactionEntity(
                    customerId = customerId,
                    amount = amount,
                    type = type,
                    notes = notes?.trim()
                )
                transactionRepository.insertTransaction(transaction)
                _snackbarMessage.emit("${type.name} transaction added successfully")
            } catch (e: Exception) {
                _snackbarMessage.emit("Error adding transaction: ${e.message}")
            }
        }
    }
    
    /**
     * Delete transaction
     */
    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            try {
                transactionRepository.deleteTransaction(transaction)
                _snackbarMessage.emit("Transaction deleted successfully")
            } catch (e: Exception) {
                _snackbarMessage.emit("Error deleting transaction: ${e.message}")
            }
        }
    }
}

/**
 * UI State data class for Transaction screens
 */
data class TransactionUiState(
    val transactions: List<TransactionEntity> = emptyList(),
    val customer: CustomerEntity? = null,
    val totalCredit: Double = 0.0,
    val totalPayment: Double = 0.0,
    val pendingBalance: Double = 0.0,
    val isLoading: Boolean = true,
    val error: String? = null
)
