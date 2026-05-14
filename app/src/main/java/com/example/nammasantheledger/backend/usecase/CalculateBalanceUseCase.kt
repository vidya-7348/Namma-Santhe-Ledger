package com.example.nammasantheledger.backend.usecase

import com.example.nammasantheledger.database.entity.TransactionEntity
import com.example.nammasantheledger.database.entity.TransactionType

/**
 * Use case for calculating customer balance
 * Calculates total credit, total payment, and pending balance
 */
class CalculateBalanceUseCase {
    
    /**
     * Calculate balance from transaction list
     * @param transactions List of transactions
     * @return BalanceData containing totals
     */
    operator fun invoke(transactions: List<TransactionEntity>): BalanceData {
        var totalCredit = 0.0
        var totalPayment = 0.0
        
        transactions.forEach { transaction ->
            when (transaction.type) {
                TransactionType.CREDIT -> totalCredit += transaction.amount
                TransactionType.PAYMENT -> totalPayment += transaction.amount
            }
        }
        
        val pendingBalance = totalCredit - totalPayment
        
        return BalanceData(
            totalCredit = totalCredit,
            totalPayment = totalPayment,
            pendingBalance = pendingBalance
        )
    }
    
    /**
     * Data class holding balance calculation results
     */
    data class BalanceData(
        val totalCredit: Double,
        val totalPayment: Double,
        val pendingBalance: Double
    )
}
