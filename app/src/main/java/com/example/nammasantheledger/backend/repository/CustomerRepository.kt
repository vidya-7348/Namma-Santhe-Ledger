package com.example.nammasantheledger.backend.repository

import com.example.nammasantheledger.database.dao.CustomerDao
import com.example.nammasantheledger.database.entity.CustomerEntity
import com.example.nammasantheledger.database.entity.CustomerWithBalance
import kotlinx.coroutines.flow.Flow

/**
 * Repository class for Customer operations
 * Acts as a single source of truth for customer data
 */
class CustomerRepository(
    private val customerDao: CustomerDao
) {
    
    /**
     * Get all customers as a Flow
     */
    fun getAllCustomers(): Flow<List<CustomerEntity>> = customerDao.getAllCustomers()
    
    /**
     * Get customer by ID as a Flow
     */
    fun getCustomerById(customerId: Long): Flow<CustomerEntity?> = customerDao.getCustomerById(customerId)
    
    /**
     * Insert a new customer
     */
    suspend fun insertCustomer(customer: CustomerEntity): Long {
        return customerDao.insertCustomer(customer)
    }
    
    /**
     * Update an existing customer
     */
    suspend fun updateCustomer(customer: CustomerEntity) {
        customerDao.updateCustomer(customer)
    }
    
    /**
     * Delete a customer
     */
    suspend fun deleteCustomer(customer: CustomerEntity) {
        customerDao.deleteCustomer(customer)
    }
    
    /**
     * Search customers by name or phone
     */
    fun searchCustomers(query: String): Flow<List<CustomerEntity>> = customerDao.searchCustomers(query)
    
    /**
     * Get all customers with their pending balance
     */
    fun getCustomersWithBalance(): Flow<List<CustomerWithBalance>> = customerDao.getCustomersWithBalance()
    
    /**
     * Get total customer count
     */
    fun getCustomerCount(): Flow<Int> = customerDao.getCustomerCount()
}
