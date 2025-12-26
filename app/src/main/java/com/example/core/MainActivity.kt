package com.example.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.core.ui.navigation.AppNavigation
import com.example.core.ui.navigation.Screen
import com.example.core.ui.theme.TestHomeWorkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            TestHomeWorkTheme {
                val navController = rememberNavController()
                
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Create, contentDescription = null) },
                                label = { Text("Перевод") },
                                selected = navController.currentDestination?.route == Screen.Translation.route,
                                onClick = {
                                    navController.navigate(Screen.Translation.route) {
                                        popUpTo(Screen.Translation.route) { inclusive = true }
                                    }
                                }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                                label = { Text("История") },
                                selected = navController.currentDestination?.route == Screen.History.route,
                                onClick = {
                                    navController.navigate(Screen.History.route) {
                                        popUpTo(Screen.Translation.route) { inclusive = false }
                                    }
                                }
                            )
                        }
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        AppNavigation(navController = navController)
                    }
                }
            }
        }
    }
}

