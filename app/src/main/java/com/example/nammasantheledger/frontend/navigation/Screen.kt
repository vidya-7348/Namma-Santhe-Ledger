package com.example.nammasantheledger.frontend.navigation

/**
 * Sealed class defining all navigation routes in the app
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Customers : Screen("customers")
    object AddCustomer : Screen("add_customer")
    object AddTransaction : Screen("add_transaction")
    object Ledger : Screen("ledger/{customerId}") {
        fun createRoute(customerId: Long) = "ledger/$customerId"
    }
    object Reports : Screen("reports")
    object Settings : Screen("settings")
    object EditCustomer : Screen("edit_customer/{customerId}") {
        fun createRoute(customerId: Long) = "edit_customer/$customerId"
    }
    object Analytics : Screen("analytics")
}
