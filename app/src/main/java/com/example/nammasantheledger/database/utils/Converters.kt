package com.example.nammasantheledger.database.utils

import androidx.room.TypeConverter
import com.example.nammasantheledger.database.entity.TransactionType

/**
 * Type converter class for Room database
 * Converts TransactionType enum to String and vice versa
 */
class Converters {
    
    /**
     * Converts TransactionType enum to String for database storage
     */
    @TypeConverter
    fun fromTransactionType(value: TransactionType): String = value.name
    
    /**
     * Converts String from database to TransactionType enum
     */
    @TypeConverter
    fun toTransactionType(value: String): TransactionType = TransactionType.valueOf(value)
}
