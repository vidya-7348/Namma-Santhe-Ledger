package com.example.nammasantheledger.database.dao

import androidx.room.*
import com.example.nammasantheledger.database.entity.CustomerEntity
import com.example.nammasantheledger.database.entity.CustomerWithBalance
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Customer operations
 * Provides methods to interact with the customers table
 */
@Dao
interface CustomerDao {
    
    /**
     * Insert a new customer
     * @param customer CustomerEntity to insert
     * @return Row ID of inserted customer
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity): Long
    
    /**
     * Update an existing customer
     * @param customer CustomerEntity with updated data
     */
    @Update
    suspend fun updateCustomer(customer: CustomerEntity)
    
    /**
     * Delete a customer
     * @param customer CustomerEntity to delete
     */
    @Delete
    suspend fun deleteCustomer(customer: CustomerEntity)
    
    /**
     * Get all customers ordered by name
     * @return Flow list of all customers
     */
    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomers(): Flow<List<CustomerEntity>>
    
    /**
     * Get customer by ID
     * @param customerId ID of customer to retrieve
     * @return Flow of CustomerEntity or null
     */
    @Query("SELECT * FROM customers WHERE id = :customerId")
    fun getCustomerById(customerId: Long): Flow<CustomerEntity?>
    
    /**
     * Search customers by name or phone
     * @param query Search query string
     * @return Flow list of matching customers
     */
    @Query("SELECT * FROM customers WHERE name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchCustomers(query: String): Flow<List<CustomerEntity>>
    
    /**
     * Get all customers with their pending balance
     * Calculates balance as: Total Credit - Total Payment
     * @return Flow list of customers with balance
     */
    @Query("""
        SELECT 
            c.id,
            c.name,
            c.phone,
            c.address,
            c.createdAt,
            COALESCE(
                (SELECT SUM(CASE WHEN t.type = 'CREDIT' THEN t.amount ELSE 0 END) 
                 FROM transactions t WHERE t.customerId = c.id) -
                (SELECT SUM(CASE WHEN t.type = 'PAYMENT' THEN t.amount ELSE 0 END) 
                 FROM transactions t WHERE t.customerId = c.id),
                0
            ) as pendingBalance
        FROM customers c
        ORDER BY c.name ASC
    """)
    fun getCustomersWithBalance(): Flow<List<CustomerWithBalance>>
    
    /**
     * Get total number of customers
     * @return Flow of customer count
     */
    @Query("SELECT COUNT(*) FROM customers")
    fun getCustomerCount(): Flow<Int>
}
