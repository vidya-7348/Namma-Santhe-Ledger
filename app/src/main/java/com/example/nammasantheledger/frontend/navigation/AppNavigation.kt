package com.example.nammasantheledger.frontend.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nammasantheledger.backend.viewmodel.*
import com.example.nammasantheledger.database.db.LedgerDatabase
import com.example.nammasantheledger.database.entity.CustomerEntity
import com.example.nammasantheledger.frontend.screens.*

/**
 * Main navigation graph for the application
 * Sets up all routes and screens with ViewModel injection
 */
@Composable
fun AppNavigation(
    database: LedgerDatabase
) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Home Dashboard
        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(
                    customerRepository = com.example.nammasantheledger.backend.repository.CustomerRepository(database.customerDao()),
                    transactionRepository = com.example.nammasantheledger.backend.repository.TransactionRepository(database.transactionDao())
                )
            )
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToCustomers = { navController.navigate(Screen.Customers.route) },
                onNavigateToAddTransaction = { navController.navigate(Screen.AddTransaction.route) },
                onNavigateToReports = { navController.navigate(Screen.Reports.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToAnalytics = { navController.navigate(Screen.Analytics.route) }
            )
        }
        
        // Customer List
        composable(Screen.Customers.route) {
            val customerViewModel: CustomerViewModel = viewModel(
                factory = CustomerViewModelFactory(
                    customerRepository = com.example.nammasantheledger.backend.repository.CustomerRepository(database.customerDao())
                )
            )
            CustomerListScreen(
                viewModel = customerViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddCustomer = { navController.navigate(Screen.AddCustomer.route) },
                onNavigateToLedger = { customerId ->
                    navController.navigate(Screen.Ledger.createRoute(customerId))
                }
            )
        }
        
        // Add Customer
        composable(Screen.AddCustomer.route) {
            val customerViewModel: CustomerViewModel = viewModel(
                factory = CustomerViewModelFactory(
                    customerRepository = com.example.nammasantheledger.backend.repository.CustomerRepository(database.customerDao())
                )
            )
            AddCustomerScreen(
                viewModel = customerViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Edit Customer
        composable(
            route = Screen.EditCustomer.route,
            arguments = listOf(navArgument("customerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getLong("customerId") ?: return@composable
            val customerViewModel: CustomerViewModel = viewModel(
                factory = CustomerViewModelFactory(
                    customerRepository = com.example.nammasantheledger.backend.repository.CustomerRepository(database.customerDao())
                )
            )
            EditCustomerScreen(
                viewModel = customerViewModel,
                customerId = customerId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Add Transaction
        composable(Screen.AddTransaction.route) {
            val transactionViewModel: TransactionViewModel = viewModel(
                factory = TransactionViewModelFactory(
                    transactionRepository = com.example.nammasantheledger.backend.repository.TransactionRepository(database.transactionDao()),
                    customerRepository = com.example.nammasantheledger.backend.repository.CustomerRepository(database.customerDao())
                )
            )
            AddTransactionScreen(
                viewModel = transactionViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Ledger Screen
        composable(
            route = Screen.Ledger.route,
            arguments = listOf(navArgument("customerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getLong("customerId") ?: return@composable
            val transactionViewModel: TransactionViewModel = viewModel(
                factory = TransactionViewModelFactory(
                    transactionRepository = com.example.nammasantheledger.backend.repository.TransactionRepository(database.transactionDao()),
                    customerRepository = com.example.nammasantheledger.backend.repository.CustomerRepository(database.customerDao())
                )
            )
            LedgerScreen(
                viewModel = transactionViewModel,
                customerId = customerId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Reports Screen
        composable(Screen.Reports.route) {
            val reportViewModel: ReportViewModel = viewModel(
                factory = ReportViewModelFactory(
                    transactionRepository = com.example.nammasantheledger.backend.repository.TransactionRepository(database.transactionDao())
                )
            )
            ReportsScreen(
                viewModel = reportViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Analytics Screen
        composable(Screen.Analytics.route) {
            val analyticsViewModel: AnalyticsViewModel = viewModel(
                factory = AnalyticsViewModelFactory(
                    analyticsRepository = com.example.nammasantheledger.backend.repository.AnalyticsRepository(
                        database.transactionDao(),
                        database.customerDao()
                    )
                )
            )
            AnalyticsScreen(
                viewModel = analyticsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Settings Screen
        composable(Screen.Settings.route) {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(
                    context = navController.context
                )
            )
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
