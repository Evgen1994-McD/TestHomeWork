package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.feature_home.presentation.HomeScreen
import com.example.feature_home.presentation.HomeViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            var showHomeScreen by remember { mutableStateOf(false) }
            
            if (showHomeScreen) {
                val viewModel: HomeViewModel = koinViewModel()
                HomeScreen(viewModel = viewModel)
            } else {
                // Экран с кнопкой для открытия Home
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { showHomeScreen = true },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Открыть Home")
                        }
                    }
                }
            }
        }
    }
}
