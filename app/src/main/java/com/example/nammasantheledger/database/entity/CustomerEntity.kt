package com.example.nammasantheledger.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing a customer in the database
 * Stores customer information including name, phone, and address
 */
@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val name: String,
    
    val phone: String,
    
    val address: String?,
    
    val createdAt: Long = System.currentTimeMillis()
)
