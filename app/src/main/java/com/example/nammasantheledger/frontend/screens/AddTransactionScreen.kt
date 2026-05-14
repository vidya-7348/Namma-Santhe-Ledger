package com.example.nammasantheledger.frontend.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nammasantheledger.backend.viewmodel.TransactionViewModel
import com.example.nammasantheledger.database.entity.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: TransactionViewModel,
    onNavigateBack: () -> Unit
) {
    val customers by viewModel.customers.collectAsState()
    
    var selectedCustomerId by remember { mutableStateOf<Long?>(null) }
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var transactionType by remember { mutableStateOf(TransactionType.CREDIT) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Transaction", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedCustomerId?.let { 
                        customers.find { c -> c.id == it }?.name ?: "Select Customer" 
                    } ?: "Select Customer",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Customer *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    customers.forEach { customer ->
                        DropdownMenuItem(
                            text = { Text(customer.name) },
                            onClick = {
                                selectedCustomerId = customer.id
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            Text("Transaction Type", fontWeight = FontWeight.Medium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    selected = transactionType == TransactionType.CREDIT,
                    onClick = { transactionType = TransactionType.CREDIT },
                    label = { Text("Credit") },
                    leadingIcon = {
                        if (transactionType == TransactionType.CREDIT) 
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        else null
                    },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = transactionType == TransactionType.PAYMENT,
                    onClick = { transactionType = TransactionType.PAYMENT },
                    label = { Text("Payment") },
                    leadingIcon = {
                        if (transactionType == TransactionType.PAYMENT) 
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        else null
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount *") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.CurrencyRupee, contentDescription = null) }
            )
            
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    selectedCustomerId?.let { customerId ->
                        amount.toDoubleOrNull()?.let { amt ->
                            viewModel.addTransaction(customerId, amt, transactionType, notes.ifBlank { null })
                            onNavigateBack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedCustomerId != null && amount.toDoubleOrNull() != null
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Transaction")
            }
        }
    }
}
