package com.example.testhomework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.testhomework.presentation.details.PhotoDetailsScreen
import com.example.testhomework.presentation.gallery.GalleryScreen
import com.example.testhomework.ui.theme.TestHomeWorkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestHomeWorkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    NavHost(
                        navController = navController,
                        startDestination = "gallery"
                    ) {
                        composable("gallery") {
                            GalleryScreen(
                                onPhotoClick = { photo ->
                                    // Используем URL encoding для безопасной передачи параметров
                                    val encodedUrl = java.net.URLEncoder.encode(photo.fullUrl, "UTF-8")
                                    val encodedTitle = java.net.URLEncoder.encode(photo.title, "UTF-8")
                                    navController.navigate("photo_details/$encodedUrl/$encodedTitle")
                                }
                            )
                        }
                        composable(
                            route = "photo_details/{photoUrl}/{photoTitle}",
                            arguments = listOf(
                                navArgument("photoUrl") { type = NavType.StringType },
                                navArgument("photoTitle") { 
                                    type = NavType.StringType
                                    defaultValue = ""
                                }
                            )
                        ) { backStackEntry ->
                            val photoUrl = java.net.URLDecoder.decode(
                                backStackEntry.arguments?.getString("photoUrl") ?: "",
                                "UTF-8"
                            )
                            val photoTitle = java.net.URLDecoder.decode(
                                backStackEntry.arguments?.getString("photoTitle") ?: "",
                                "UTF-8"
                            )
                            
                            PhotoDetailsScreen(
                                photoUrl = photoUrl,
                                photoTitle = photoTitle,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
