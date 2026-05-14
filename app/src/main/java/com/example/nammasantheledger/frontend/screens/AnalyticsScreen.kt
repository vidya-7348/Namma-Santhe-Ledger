package com.example.nammasantheledger.frontend.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nammasantheledger.backend.viewmodel.AnalyticsViewModel
import com.example.nammasantheledger.database.entity.AnalyticsSummary
import com.example.nammasantheledger.database.entity.CustomerEntity
import com.example.nammasantheledger.database.entity.MonthlyPendingData
import com.example.nammasantheledger.database.entity.CustomerPendingData
import com.example.nammasantheledger.frontend.theme.*
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel,
    onNavigateBack: () -> Unit
) {
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val selectedYear by viewModel.selectedYear.collectAsState()
    val selectedCustomerId by viewModel.selectedCustomerId.collectAsState()
    val customers by viewModel.customers.collectAsState()
    val customerTrendData by viewModel.customerTrendData.collectAsState()
    val customerComparison by viewModel.customerComparison.collectAsState()
    val summary by viewModel.summary.collectAsState()

    var showMonthPicker by remember { mutableStateOf(false) }
    var showCustomerPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(PrimaryTeal, PrimaryTealDark)
                        )
                    )
                    .statusBarsPadding()
                    .padding(vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Customer Pending Analytics",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (selectedCustomerId == null) "Comparison across all customers" else "Monthly trend for selected customer",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Filters Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        modifier = Modifier.weight(1f),
                        label = getMonthName(selectedMonth) + " " + selectedYear,
                        icon = Icons.Default.CalendarMonth,
                        onClick = { showMonthPicker = true }
                    )
                    FilterChip(
                        modifier = Modifier.weight(1f),
                        label = customers.find { it.id == selectedCustomerId }?.name ?: "All Customers",
                        icon = Icons.Default.Person,
                        onClick = { showCustomerPicker = true }
                    )
                }
            }

            // Summary Stats Section
            item {
                AnalyticsSummaryCards(summary, isSingleCustomer = selectedCustomerId != null)
            }

            // Graph Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (selectedCustomerId == null) "Customer Comparison" else "Pending Trend (${selectedYear})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = if (selectedCustomerId == null) Icons.Default.BarChart else Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = null,
                                tint = PrimaryTeal,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
                            if (selectedCustomerId == null) {
                                // Comparison Chart for All Customers
                                if (customerComparison.isEmpty()) {
                                    EmptyChartUI("No customer data for this month")
                                } else {
                                    CustomerComparisonChart(customerComparison)
                                }
                            } else {
                                // Trend Chart for Single Customer
                                if (customerTrendData.isEmpty()) {
                                    EmptyChartUI("No trend data available")
                                } else {
                                    CustomerTrendChart(customerTrendData)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Dialogs
        if (showMonthPicker) {
            MonthYearPickerDialog(
                currentMonth = selectedMonth.toInt(),
                currentYear = selectedYear,
                onDismiss = { showMonthPicker = false },
                onMonthYearSelected = { month, year ->
                    viewModel.updateMonth(month)
                    viewModel.updateYear(year)
                    showMonthPicker = false
                }
            )
        }

        if (showCustomerPicker) {
            CustomerPickerDialog(
                customers = customers,
                selectedCustomerId = selectedCustomerId,
                onDismiss = { showCustomerPicker = false },
                onCustomerSelected = {
                    viewModel.selectCustomer(it)
                    showCustomerPicker = false
                }
            )
        }
    }
}

@Composable
fun CustomerComparisonChart(data: List<CustomerPendingData>) {
    val entries = data.mapIndexed { index, item ->
        entryOf(index.toFloat(), item.pendingAmount.toFloat())
    }
    
    val nameFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        data.getOrNull(value.toInt())?.customerName ?: ""
    }

    Chart(
        chart = columnChart(),
        model = entryModelOf(entries),
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(valueFormatter = nameFormatter),
        modifier = Modifier.fillMaxSize(),
        chartScrollState = rememberChartScrollState()
    )
}

@Composable
fun CustomerTrendChart(data: List<MonthlyPendingData>) {
    // Map month numbers to names for trend
    val entries = data.map { item ->
        entryOf(item.date.toFloat(), item.pendingAmount.toFloat())
    }

    val monthFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        months.getOrNull(value.toInt() - 1) ?: ""
    }

    Chart(
        chart = lineChart(
            spacing = 40.dp,
            targetVerticalAxisPosition = AxisPosition.Vertical.Start
        ),
        model = entryModelOf(entries),
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(valueFormatter = monthFormatter),
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun EmptyChartUI(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(message, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = PrimaryTeal)
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, style = MaterialTheme.typography.labelLarge, maxLines = 1, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AnalyticsSummaryCards(summary: AnalyticsSummary, isSingleCustomer: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Total Pending",
                value = "₹${summary.totalPending}",
                icon = Icons.Default.AccountBalanceWallet,
                brush = Brush.horizontalGradient(listOf(ErrorRed, Color(0xFFFF5252))),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = if (isSingleCustomer) "Selected Month" else "Top Customer",
                value = if (isSingleCustomer) "Selected" else summary.topCustomer,
                icon = if (isSingleCustomer) Icons.Default.CalendarToday else Icons.Default.Star,
                brush = Brush.horizontalGradient(listOf(WarningOrange, Color(0xFFFFB74D))),
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Total Credits",
                value = "₹${summary.totalCredits}",
                icon = Icons.Default.ArrowCircleUp,
                brush = Brush.horizontalGradient(listOf(PrimaryTeal, PrimaryTealLight)),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Total Payments",
                value = "₹${summary.totalPayments}",
                icon = Icons.Default.ArrowCircleDown,
                brush = Brush.horizontalGradient(listOf(SuccessGreen, SuccessGreenLight)),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    icon: ImageVector,
    brush: Brush,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush)
                .padding(16.dp)
        ) {
            Column {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text(title, style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.8f))
                Text(
                    value, 
                    style = MaterialTheme.typography.titleLarge, 
                    fontWeight = FontWeight.Bold, 
                    color = Color.White,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun MonthYearPickerDialog(
    currentMonth: Int,
    currentYear: String,
    onDismiss: () -> Unit,
    onMonthYearSelected: (Int, String) -> Unit
) {
    val months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    val years = (2023..2030).map { it.toString() }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Month & Year") },
        text = {
            Column {
                Text("Month", style = MaterialTheme.typography.labelLarge)
                ScrollableSelector(items = months.toList(), selectedIndex = currentMonth - 1) { index ->
                    onMonthYearSelected(index + 1, currentYear)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Year", style = MaterialTheme.typography.labelLarge)
                ScrollableSelector(items = years, selectedIndex = years.indexOf(currentYear).coerceAtLeast(0)) { index ->
                    onMonthYearSelected(currentMonth, years[index])
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close") } }
    )
}

@Composable
fun ScrollableSelector(items: List<String>, selectedIndex: Int, onSelect: (Int) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
        LazyColumn {
            itemsIndexed(items) { index, item ->
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(index) }
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .background(if (index == selectedIndex) PrimaryTeal.copy(alpha = 0.1f) else Color.Transparent),
                    color = if (index == selectedIndex) PrimaryTeal else Color.Black,
                    fontWeight = if (index == selectedIndex) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerPickerDialog(
    customers: List<CustomerEntity>,
    selectedCustomerId: Long?,
    onDismiss: () -> Unit,
    onCustomerSelected: (Long?) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredCustomers = customers.filter { it.name.contains(searchQuery, ignoreCase = true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Customer") },
        text = {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search Customer") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    LazyColumn {
                        item {
                            ListItem(
                                headlineContent = { Text("All Customers", fontWeight = FontWeight.Bold) },
                                modifier = Modifier.clickable { onCustomerSelected(null) },
                                leadingContent = { Icon(Icons.Default.People, contentDescription = null, tint = PrimaryTeal) },
                                trailingContent = { if (selectedCustomerId == null) Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PrimaryTeal) else null }
                            )
                        }
                        items(filteredCustomers) { customer ->
                            ListItem(
                                headlineContent = { Text(customer.name) },
                                modifier = Modifier.clickable { onCustomerSelected(customer.id) },
                                leadingContent = { Icon(Icons.Default.Person, contentDescription = null) },
                                trailingContent = { if (selectedCustomerId == customer.id) Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PrimaryTeal) else null }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

private fun getMonthName(month: String): String {
    val months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    return try {
        months[month.toInt() - 1]
    } catch (e: Exception) {
        month
    }
}
