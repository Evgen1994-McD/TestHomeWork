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
import java.net.URLDecoder
import java.net.URLEncoder

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
                                    val encodedUrl = URLEncoder.encode(photo.fullUrl, "UTF-8")
                                    val baseUrl = URLEncoder.encode(photo.thumbnailUrl, "UTF-8")
                                    val encodedTitle = URLEncoder.encode(photo.title, "UTF-8")
                                    navController.navigate("photo_details/$encodedUrl/$encodedTitle/$baseUrl")
                                }
                            )
                        }
                        composable(
                            route = "photo_details/{photoUrl}/{photoTitle}/{baseUrl}",
                            arguments = listOf(
                                navArgument("photoUrl") { type = NavType.StringType },
                                navArgument("photoTitle") { 
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                                navArgument("baseUrl") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val photoUrl = URLDecoder.decode(
                                backStackEntry.arguments?.getString("photoUrl") ?: "",
                                "UTF-8"
                            )
                            val baseUrl = URLDecoder.decode(
                                backStackEntry.arguments?.getString("baseUrl") ?: "",
                                "UTF-8"
                            )
                            val photoTitle = URLDecoder.decode(
                                backStackEntry.arguments?.getString("photoTitle") ?: "",
                                "UTF-8"
                            )
                            
                            PhotoDetailsScreen(
                                photoUrl = photoUrl,
                                photoTitle = photoTitle,
                                onBackClick = { navController.popBackStack() },
                                photoBaseUrl = baseUrl
                            )
                        }
                    }
                }
            }
        }
    }
}
