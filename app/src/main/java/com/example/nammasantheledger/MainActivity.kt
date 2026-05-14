package com.example.nammasantheledger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.nammasantheledger.database.db.LedgerDatabase
import com.example.nammasantheledger.frontend.navigation.AppNavigation
import com.example.nammasantheledger.frontend.theme.NammaSantheLedgerTheme

class MainActivity : ComponentActivity() {
    
    private lateinit var database: LedgerDatabase
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize database
        database = LedgerDatabase.getInstance(applicationContext)
        
        setContent {
            NammaSantheLedgerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(database = database)
                }
            }
        }
    }
}
