package com.example.nammasantheledger.backend.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility object for date formatting and calculations
 */
object DateUtils {
    
    private val displayDateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    private val shortDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    
    /**
     * Format timestamp to display format (date + time)
     */
    fun formatTimestamp(timestamp: Long): String {
        return displayDateFormat.format(Date(timestamp))
    }
    
    /**
     * Format timestamp to short date format
     */
    fun formatDate(timestamp: Long): String {
        return shortDateFormat.format(Date(timestamp))
    }
    
    /**
     * Format timestamp to time only
     */
    fun formatTime(timestamp: Long): String {
        return timeFormat.format(Date(timestamp))
    }
    
    /**
     * Get start of today (midnight)
     */
    fun getStartOfDay(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    /**
     * Get start of current week (Monday)
     */
    fun getStartOfWeek(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        return calendar.timeInMillis
    }
    
    /**
     * Get start of current month
     */
    fun getStartOfMonth(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    /**
     * Get end of current day
     */
    fun getEndOfDay(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }
    
    /**
     * Get current timestamp
     */
    fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis()
    }
    
    /**
     * Get relative time description (Today, Yesterday, etc.)
     */
    fun getRelativeTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        
        return when {
            diff < 60 * 60 * 1000 -> "Just now"
            diff < 24 * 60 * 60 * 1000 -> "Today"
            diff < 2 * 24 * 60 * 60 * 1000 -> "Yesterday"
            else -> formatDate(timestamp)
        }
    }
}
