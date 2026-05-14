package com.example.nammasantheledger.backend.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helper object for WhatsApp integration
 * Provides functionality to send reminder messages via WhatsApp
 */
object WhatsAppHelper {
    
    /**
     * Open WhatsApp with pre-filled reminder message
     * @param context Android context
     * @param phoneNumber Customer's phone number
     * @param customerName Customer's name
     * @param amount Pending amount
     */
    fun sendWhatsAppReminder(
        context: Context,
        phoneNumber: String,
        customerName: String,
        amount: Double
    ) {
        val message = "Hello $customerName, your pending amount is ₹${String.format("%.2f", amount)}. Kindly pay when possible."
        val encodedMessage = Uri.encode(message)
        
        // Format phone number (remove spaces, ensure country code)
        val formattedPhone = phoneNumber.replace("\\s+".toRegex(), "")
        
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://api.whatsapp.com/send?phone=$formattedPhone&text=$encodedMessage")
            `package` = "com.whatsapp"
        }
        
        // Check if WhatsApp is installed
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }
}
