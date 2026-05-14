package com.example.nammasantheledger.frontend.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nammasantheledger.backend.utils.CurrencyFormatter
import com.example.nammasantheledger.backend.viewmodel.ReportViewModel
import com.example.nammasantheledger.backend.viewmodel.ReportUiState
import com.example.nammasantheledger.frontend.components.DashboardCard
import com.example.nammasantheledger.frontend.components.EmptyState
import com.example.nammasantheledger.frontend.components.LoadingIndicator
import com.example.nammasantheledger.frontend.components.TransactionCard
import com.example.nammasantheledger.frontend.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    viewModel: ReportViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedPeriod by remember { mutableStateOf("Today") }

    LaunchedEffect(selectedPeriod) {
        when (selectedPeriod) {
            "Today" -> viewModel.loadTodayReport()
            "Week" -> viewModel.loadWeeklyReport()
            "Month" -> viewModel.loadMonthlyReport()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reports", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            LoadingIndicator(modifier = Modifier.padding(paddingValues))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Today", "Week", "Month").forEach { period ->
                            FilterChip(
                                selected = selectedPeriod == period,
                                onClick = { selectedPeriod = period },
                                label = { Text(period) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                item {
                    DashboardCard(
                        title = "Total Credit",
                        value = CurrencyFormatter.formatAmount(uiState.totalCredit),
                        icon = Icons.Default.TrendingUp,
                        gradientStart = WarningOrange,
                        gradientEnd = Color(0xFFFFB84D)
                    )
                }

                item {
                    DashboardCard(
                        title = "Total Payment",
                        value = CurrencyFormatter.formatAmount(uiState.totalPayment),
                        icon = Icons.Default.TrendingDown,
                        gradientStart = SuccessGreen,
                        gradientEnd = SuccessGreenLight
                    )
                }

                item {
                    DashboardCard(
                        title = "Net Balance",
                        value = CurrencyFormatter.formatAmount(uiState.netBalance),
                        icon = Icons.Default.AccountBalanceWallet,
                        gradientStart = ErrorRed,
                        gradientEnd = ErrorRedLight
                    )
                }

                if (uiState.transactions.isEmpty()) {
                    item {
                        EmptyState(message = "No transactions for ${uiState.period.lowercase()}")
                    }
                } else {
                    items(uiState.transactions) { transaction ->
                        TransactionCard(transaction = transaction)
                    }
                }
            }
        }
    }
}
