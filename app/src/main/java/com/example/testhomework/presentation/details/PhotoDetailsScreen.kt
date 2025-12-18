package com.example.testhomework.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.testhomework.R
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailsScreen(
    photoUrl: String,
    photoTitle: String,
    onBackClick: () -> Unit,
    viewModel: PhotoDetailsViewModel = koinViewModel()
) {
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val transformableState = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offsetX += offsetChange.x
        offsetY += offsetChange.y
    }

    val uiState by viewModel.uiState.observeAsState()
    
    // Получаем размер экрана для ограничения размера изображения
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val context = LocalContext.current

    // Загружаем фото при первом запуске
    LaunchedEffect(photoUrl, photoTitle) {
        android.util.Log.d("PhotoDetailsScreen", "Loading photo: url=$photoUrl, title=$photoTitle")
        viewModel.loadPhoto(photoUrl, photoTitle)
    }
    
    // Таймаут для ошибки загрузки
    LaunchedEffect(uiState) {
        if (uiState is PhotoDetailsUiState.Loading) {
            delay(10000) // 10 секунд таймаут
            val stateAfterDelay = viewModel.uiState.value
            if (stateAfterDelay is PhotoDetailsUiState.Loading) {
                android.util.Log.e("PhotoDetailsScreen", "Timeout loading image: $photoUrl")
                viewModel.onImageLoadError(Exception("Timeout loading image"))
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(photoTitle) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                null -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is PhotoDetailsUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                        // Показываем изображение сразу, даже в состоянии Loading
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(state.photoUrl)
                                .size(Size(screenWidth, screenHeight))
                                .crossfade(true)
                                .build(),
                            contentDescription = state.photoTitle,
                            contentScale = ContentScale.Fit,
                            onSuccess = { 
                                android.util.Log.d("PhotoDetailsScreen", "Image loaded successfully: ${state.photoUrl}")
                                viewModel.onImageLoaded()
                            },
                            onError = { error ->
                                android.util.Log.e("PhotoDetailsScreen", "Image load error: ${error.result.throwable.message}", error.result.throwable)
                                viewModel.onImageLoadError(error.result.throwable)
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer(
                                    scaleX = scale,
                                    scaleY = scale,
                                    rotationZ = rotation,
                                    translationX = offsetX,
                                    translationY = offsetY
                                )
                                .transformable(state = transformableState)
                        )
                    }
                }
                is PhotoDetailsUiState.Success -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(state.photoUrl)
                                .size(Size(screenWidth, screenHeight))
                                .crossfade(true)
                                .build(),
                            contentDescription = state.photoTitle,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer(
                                    scaleX = scale,
                                    scaleY = scale,
                                    rotationZ = rotation,
                                    translationX = offsetX,
                                    translationY = offsetY
                                )
                                .transformable(state = transformableState)
                        )
                    }
                }
                is PhotoDetailsUiState.Error -> {
                    ErrorContent(
                        message = state.message,
                        onRetry = {
                            viewModel.loadPhoto(state.photoUrl, state.photoTitle)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> {
                    // This should never happen for a sealed class, but added for exhaustiveness
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            Button(onClick = onRetry) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}
