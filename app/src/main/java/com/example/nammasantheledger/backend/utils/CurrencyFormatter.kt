package com.example.nammasantheledger.backend.utils

import java.text.NumberFormat
import java.util.*

/**
 * Utility object for currency formatting
 */
object CurrencyFormatter {
    
    private var currencySymbol = "₹"
    
    /**
     * Set custom currency symbol
     */
    fun setCurrencySymbol(symbol: String) {
        currencySymbol = symbol
    }
    
    /**
     * Get current currency symbol
     */
    fun getCurrencySymbol(): String = currencySymbol
    
    /**
     * Format amount with currency symbol
     */
    fun formatAmount(amount: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US)
        formatter.minimumFractionDigits = 2
        formatter.maximumFractionDigits = 2
        return "$currencySymbol${formatter.format(amount)}"
    }
    
    /**
     * Format amount without decimals
     */
    fun formatAmountShort(amount: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US)
        formatter.minimumFractionDigits = 0
        formatter.maximumFractionDigits = 0
        return "$currencySymbol${formatter.format(amount)}"
    }
    
    /**
     * Format amount with Indian numbering system (lakhs, crores)
     */
    fun formatIndianCurrency(amount: Double): String {
        return when {
            amount >= 10000000 -> "$currencySymbol${amount / 10000000}Cr"
            amount >= 100000 -> "$currencySymbol${amount / 100000}L"
            else -> formatAmount(amount)
        }
    }
}
