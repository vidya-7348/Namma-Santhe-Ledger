package com.example.nammasantheledger.frontend.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nammasantheledger.frontend.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit
) {
    val alpha = animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1500),
        label = "alpha"
    )
    
    LaunchedEffect(Unit) {
        delay(2000)
        onNavigateToHome()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(GradientStart, GradientEnd)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Assignment,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = TextOnPrimary.copy(alpha = alpha.value)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Namma-Santhe Ledger",
                style = MaterialTheme.typography.displayMedium,
                color = TextOnPrimary.copy(alpha = alpha.value),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Digital Khata for Village Markets",
                style = MaterialTheme.typography.bodyLarge,
                color = TextOnPrimary.copy(alpha = 0.8f)
            )
        }
    }
}
