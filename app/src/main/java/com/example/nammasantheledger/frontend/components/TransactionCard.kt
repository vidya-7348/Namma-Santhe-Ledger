package com.example.nammasantheledger.frontend.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nammasantheledger.database.entity.TransactionEntity
import com.example.nammasantheledger.database.entity.TransactionType
import com.example.nammasantheledger.backend.utils.CurrencyFormatter
import com.example.nammasantheledger.backend.utils.DateUtils
import com.example.nammasantheledger.frontend.theme.*

@Composable
fun TransactionCard(
    transaction: TransactionEntity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (transaction.type == TransactionType.CREDIT) 
                        Icons.Default.AddCircle else Icons.Default.RemoveCircle,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = if (transaction.type == TransactionType.CREDIT) ErrorRed else SuccessGreen
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (transaction.type == TransactionType.CREDIT) "Credit Given" else "Payment Received",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = DateUtils.formatTimestamp(transaction.timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    if (!transaction.notes.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = transaction.notes,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextLight
                        )
                    }
                }
            }
            
            Text(
                text = CurrencyFormatter.formatAmount(transaction.amount),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (transaction.type == TransactionType.CREDIT) ErrorRed else SuccessGreen
            )
        }
    }
}
