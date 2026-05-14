package com.example.nammasantheledger.frontend.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nammasantheledger.backend.utils.CurrencyFormatter
import com.example.nammasantheledger.backend.utils.WhatsAppHelper
import com.example.nammasantheledger.backend.viewmodel.TransactionViewModel
import com.example.nammasantheledger.backend.viewmodel.TransactionUiState
import com.example.nammasantheledger.frontend.components.EmptyState
import com.example.nammasantheledger.frontend.components.LoadingIndicator
import com.example.nammasantheledger.frontend.components.TransactionCard
import com.example.nammasantheledger.frontend.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LedgerScreen(
    viewModel: TransactionViewModel,
    customerId: Long,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    LaunchedEffect(customerId) {
        viewModel.loadCustomerLedger(customerId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = uiState.customer?.name ?: "Ledger",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState.customer != null && uiState.pendingBalance > 0) {
                        IconButton(
                            onClick = {
                                uiState.customer?.let { customer ->
                                    WhatsAppHelper.sendWhatsAppReminder(
                                        context = context,
                                        phoneNumber = customer.phone,
                                        customerName = customer.name,
                                        amount = uiState.pendingBalance
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Send WhatsApp Reminder",
                                tint = SuccessGreen
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            LoadingIndicator(modifier = Modifier.padding(paddingValues))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = PrimaryTeal
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Pending Balance",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = CurrencyFormatter.formatAmount(uiState.pendingBalance),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total Credit: ${CurrencyFormatter.formatAmount(uiState.totalCredit)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "Total Payment: ${CurrencyFormatter.formatAmount(uiState.totalPayment)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
                
                if (uiState.transactions.isEmpty()) {
                    EmptyState(message = "No transactions yet")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.transactions) { transaction ->
                            TransactionCard(transaction = transaction)
                        }
                    }
                }
            }
        }
    }
}
