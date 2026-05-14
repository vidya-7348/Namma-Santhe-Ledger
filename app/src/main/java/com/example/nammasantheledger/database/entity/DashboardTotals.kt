package com.example.nammasantheledger.database.entity

/**
 * Data class holding dashboard totals
 * Used for displaying summary statistics on home screen
 */
data class DashboardTotals(
    val totalOutstanding: Double = 0.0,
    val todayCredit: Double = 0.0,
    val todayPayment: Double = 0.0,
    val totalCustomers: Int = 0
)
