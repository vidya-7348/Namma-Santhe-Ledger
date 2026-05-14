package com.example.nammasantheledger.frontend.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.nammasantheledger.frontend.theme.*

@Composable
fun DashboardCard(
    title: String,
    value: String,
    icon: ImageVector,
    gradientStart: Color = GradientStart,
    gradientEnd: Color = GradientEnd,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(gradientStart, gradientEnd)
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TextOnPrimary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextOnPrimary.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextOnPrimary
                )
            }
        }
    }
}
