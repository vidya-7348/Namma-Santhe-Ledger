package com.example.nammasantheledger.frontend.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nammasantheledger.backend.viewmodel.CustomerViewModel
import com.example.nammasantheledger.database.entity.CustomerEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCustomerScreen(
    viewModel: CustomerViewModel,
    customerId: Long,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var customerLoaded by remember { mutableStateOf(false) }
    
    LaunchedEffect(customerId) {
        viewModel.uiState.collect { state ->
            // Load existing customer data here if needed
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Customer", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name *") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
            )
            
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone *") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
            )
            
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank()) {
                        val customer = CustomerEntity(
                            id = customerId,
                            name = name,
                            phone = phone,
                            address = address.ifBlank { null }
                        )
                        viewModel.updateCustomer(customer)
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() && phone.isNotBlank()
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Update Customer")
            }
        }
    }
}
