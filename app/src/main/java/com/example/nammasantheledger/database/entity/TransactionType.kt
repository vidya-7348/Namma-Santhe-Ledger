package com.example.nammasantheledger.database.entity

/**
 * Enum class representing the type of transaction
 * CREDIT - When vendor gives goods on credit (customer owes money)
 * PAYMENT - When customer pays back the due amount
 */
enum class TransactionType {
    CREDIT,
    PAYMENT
}
