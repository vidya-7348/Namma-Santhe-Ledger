package com.example.nammasantheledger.database.entity

/**
 * Data class representing a customer with their pending balance
 * Used for displaying customer list with outstanding amounts
 */
data class CustomerWithBalance(
    val id: Long,
    val name: String,
    val phone: String,
    val address: String?,
    val createdAt: Long,
    val pendingBalance: Double
)
