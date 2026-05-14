package com.example.nammasantheledger.database.entity

/**
 * Data model for daily pending analytics within a month
 */
data class MonthlyPendingData(
    val date: String,
    val pendingAmount: Double
)

/**
 * Data model for customer-wise pending comparison
 */
data class CustomerPendingData(
    val customerName: String,
    val pendingAmount: Double
)

/**
 * Data model for summary stats in analytics
 */
data class AnalyticsSummary(
    val totalPending: Double = 0.0,
    val totalCredits: Double = 0.0,
    val totalPayments: Double = 0.0,
    val highestPendingDay: String = "-",
    val topCustomer: String = "-"
)
