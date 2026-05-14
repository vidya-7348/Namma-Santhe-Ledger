package com.example.nammasantheledger.backend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammasantheledger.backend.repository.CustomerRepository
import com.example.nammasantheledger.database.entity.CustomerEntity
import com.example.nammasantheledger.database.entity.CustomerWithBalance
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for Customer screens
 * Manages customer CRUD operations and state
 */
class CustomerViewModel(
    private val customerRepository: CustomerRepository
) : ViewModel() {
    
    // UI State
    private val _uiState = MutableStateFlow(CustomerUiState())
    val uiState: StateFlow<CustomerUiState> = _uiState.asStateFlow()
    
    // Snackbar message
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()
    
    init {
        loadCustomers()
    }
    
    /**
     * Load all customers with balance
     */
    private fun loadCustomers() {
        viewModelScope.launch {
            customerRepository.getCustomersWithBalance()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
                .collect { customers ->
                    _uiState.value = _uiState.value.copy(
                        customers = customers,
                        isLoading = false
                    )
                }
        }
    }
    
    /**
     * Search customers by query
     */
    fun searchCustomers(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                loadCustomers()
            } else {
                customerRepository.searchCustomers(query)
                    .collect { customers ->
                        // Convert to CustomerWithBalance (with 0 balance for search)
                        val customersWithBalance = customers.map {
                            CustomerWithBalance(
                                id = it.id,
                                name = it.name,
                                phone = it.phone,
                                address = it.address,
                                createdAt = it.createdAt,
                                pendingBalance = 0.0
                            )
                        }
                        _uiState.value = _uiState.value.copy(
                            customers = customersWithBalance,
                            isSearching = true
                        )
                    }
            }
        }
    }
    
    /**
     * Add new customer
     */
    fun addCustomer(name: String, phone: String, address: String?) {
        viewModelScope.launch {
            try {
                val customer = CustomerEntity(
                    name = name.trim(),
                    phone = phone.trim(),
                    address = address?.trim()
                )
                customerRepository.insertCustomer(customer)
                _snackbarMessage.emit("Customer added successfully")
            } catch (e: Exception) {
                _snackbarMessage.emit("Error adding customer: ${e.message}")
            }
        }
    }
    
    /**
     * Update existing customer
     */
    fun updateCustomer(customer: CustomerEntity) {
        viewModelScope.launch {
            try {
                customerRepository.updateCustomer(customer)
                _snackbarMessage.emit("Customer updated successfully")
            } catch (e: Exception) {
                _snackbarMessage.emit("Error updating customer: ${e.message}")
            }
        }
    }
    
    /**
     * Delete customer
     */
    fun deleteCustomer(customer: CustomerEntity) {
        viewModelScope.launch {
            try {
                customerRepository.deleteCustomer(customer)
                _snackbarMessage.emit("Customer deleted successfully")
            } catch (e: Exception) {
                _snackbarMessage.emit("Error deleting customer: ${e.message}")
            }
        }
    }
    
    /**
     * Clear search and reload all customers
     */
    fun clearSearch() {
        _uiState.value = _uiState.value.copy(isSearching = false)
        loadCustomers()
    }
}

/**
 * UI State data class for Customer screens
 */
data class CustomerUiState(
    val customers: List<CustomerWithBalance> = emptyList(),
    val isLoading: Boolean = true,
    val isSearching: Boolean = false,
    val error: String? = null
)
