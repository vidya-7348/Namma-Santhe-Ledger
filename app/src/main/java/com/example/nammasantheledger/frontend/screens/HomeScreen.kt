package com.example.nammasantheledger.frontend.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nammasantheledger.backend.viewmodel.HomeViewModel
import com.example.nammasantheledger.backend.viewmodel.HomeUiState
import com.example.nammasantheledger.backend.utils.CurrencyFormatter
import com.example.nammasantheledger.frontend.components.DashboardCard
import com.example.nammasantheledger.frontend.components.LoadingIndicator
import com.example.nammasantheledger.frontend.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToCustomers: () -> Unit,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAnalytics: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Namma-Santhe Ledger",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryTeal,
                    titleContentColor = TextOnPrimary,
                    actionIconContentColor = TextOnPrimary
                )
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
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DashboardCard(
                            title = "Total Outstanding",
                            value = CurrencyFormatter.formatAmount(uiState.totalOutstanding),
                            icon = Icons.Default.AccountBalanceWallet,
                            gradientStart = ErrorRed,
                            gradientEnd = ErrorRedLight,
                            modifier = Modifier.weight(1f)
                        )
                        DashboardCard(
                            title = "Total Customers",
                            value = uiState.totalCustomers.toString(),
                            icon = Icons.Default.People,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DashboardCard(
                            title = "Today Credit",
                            value = CurrencyFormatter.formatAmount(uiState.todayCredit),
                            icon = Icons.Default.TrendingUp,
                            gradientStart = WarningOrange,
                            gradientEnd = Color(0xFFFFB84D),
                            modifier = Modifier.weight(1f)
                        )
                        DashboardCard(
                            title = "Today Payment",
                            value = CurrencyFormatter.formatAmount(uiState.todayPayment),
                            icon = Icons.Default.TrendingDown,
                            gradientStart = SuccessGreen,
                            gradientEnd = SuccessGreenLight,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                item {
                    Text(
                        text = "Quick Actions",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickActionButton(
                            icon = Icons.Default.PersonAdd,
                            label = "Add Customer",
                            onClick = onNavigateToCustomers,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionButton(
                            icon = Icons.Default.AddCircle,
                            label = "Add Credit",
                            onClick = onNavigateToAddTransaction,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickActionButton(
                            icon = Icons.Default.Payment,
                            label = "Add Payment",
                            onClick = onNavigateToAddTransaction,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionButton(
                            icon = Icons.Default.Assessment,
                            label = "Reports",
                            onClick = onNavigateToReports,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickActionButton(
                            icon = Icons.Default.Analytics,
                            label = "Customer Analytics",
                            onClick = onNavigateToAnalytics,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, style = MaterialTheme.typography.labelMedium)
        }
    }
}
