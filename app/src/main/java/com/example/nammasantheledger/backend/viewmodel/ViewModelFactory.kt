package com.example.nammasantheledger.backend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nammasantheledger.backend.repository.CustomerRepository
import com.example.nammasantheledger.backend.repository.TransactionRepository
import com.example.nammasantheledger.backend.repository.AnalyticsRepository
import android.content.Context

/**
 * Factory for HomeViewModel
 */
class HomeViewModelFactory(
    private val customerRepository: CustomerRepository,
    private val transactionRepository: TransactionRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(customerRepository, transactionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Factory for CustomerViewModel
 */
class CustomerViewModelFactory(
    private val customerRepository: CustomerRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            return CustomerViewModel(customerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Factory for TransactionViewModel
 */
class TransactionViewModelFactory(
    private val transactionRepository: TransactionRepository,
    private val customerRepository: CustomerRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(transactionRepository, customerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Factory for ReportViewModel
 */
class ReportViewModelFactory(
    private val transactionRepository: TransactionRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportViewModel::class.java)) {
            return ReportViewModel(transactionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Factory for AnalyticsViewModel
 */
class AnalyticsViewModelFactory(
    private val analyticsRepository: AnalyticsRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnalyticsViewModel::class.java)) {
            return AnalyticsViewModel(analyticsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Factory for SettingsViewModel
 */
class SettingsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
