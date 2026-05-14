package com.example.nammasantheledger.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity class representing a transaction in the database
 * Links to a customer and records credit or payment transactions
 */
@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = CustomerEntity::class,
            parentColumns = ["id"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["customerId"])]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val customerId: Long,
    
    val amount: Double,
    
    val type: TransactionType,
    
    val notes: String?,
    
    val timestamp: Long = System.currentTimeMillis()
)
