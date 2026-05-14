package com.example.nammasantheledger.backend.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Extension property for DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * ViewModel for Settings screen
 * Manages app preferences using DataStore
 */
class SettingsViewModel(
    context: Context
) : ViewModel() {
    
    private val dataStore = context.dataStore
    
    // UI State
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadSettings()
    }
    
    /**
     * Load settings from DataStore
     */
    private fun loadSettings() {
        viewModelScope.launch {
            dataStore.data
                .catch { e ->
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
                .collect { preferences ->
                    _uiState.value = SettingsUiState(
                        shopName = preferences[PreferencesKeys.SHOP_NAME] ?: "",
                        currencySymbol = preferences[PreferencesKeys.CURRENCY_SYMBOL] ?: "₹"
                    )
                }
        }
    }
    
    /**
     * Update shop name
     */
    fun updateShopName(name: String) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.SHOP_NAME] = name
            }
        }
    }
    
    /**
     * Update currency symbol
     */
    fun updateCurrencySymbol(symbol: String) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.CURRENCY_SYMBOL] = symbol
            }
        }
    }
    
    /**
     * Object holding preference keys
     */
    private object PreferencesKeys {
        val SHOP_NAME = stringPreferencesKey("shop_name")
        val CURRENCY_SYMBOL = stringPreferencesKey("currency_symbol")
    }
}

/**
 * UI State data class for Settings screen
 */
data class SettingsUiState(
    val shopName: String = "",
    val currencySymbol: String = "₹",
    val error: String? = null
)
