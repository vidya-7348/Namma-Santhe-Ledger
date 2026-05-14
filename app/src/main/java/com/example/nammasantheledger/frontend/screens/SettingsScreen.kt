package com.example.nammasantheledger.frontend.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nammasantheledger.backend.viewmodel.SettingsViewModel
import com.example.nammasantheledger.backend.viewmodel.SettingsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var shopName by remember { mutableStateOf(uiState.shopName) }
    var currencySymbol by remember { mutableStateOf(uiState.currencySymbol) }
    
    LaunchedEffect(uiState) {
        shopName = uiState.shopName
        currencySymbol = uiState.currencySymbol
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
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
                value = shopName,
                onValueChange = { 
                    shopName = it
                    viewModel.updateShopName(it)
                },
                label = { Text("Shop Name") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Store, contentDescription = null) }
            )
            
            OutlinedTextField(
                value = currencySymbol,
                onValueChange = { 
                    currencySymbol = it
                    viewModel.updateCurrencySymbol(it)
                },
                label = { Text("Currency Symbol") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.CurrencyRupee, contentDescription = null) }
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            
            Text(
                text = "About",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Namma-Santhe Ledger",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Version 1.0",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "A digital ledger app for village market vendors to track credit and payments.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
