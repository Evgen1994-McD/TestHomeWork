package com.example.testhomework.presentation.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.testhomework.R
import com.example.testhomework.domain.model.Photo
import org.koin.androidx.compose.koinViewModel

@Composable
fun GalleryScreen(
    onPhotoClick: (Photo) -> Unit,
    viewModel: GalleryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.observeAsState()
    val gridState = rememberLazyGridState()

    // Загружаем первую страницу при первом запуске
    LaunchedEffect(Unit) {
        val currentState = viewModel.uiState.value
        // Загружаем только если нет данных
        if (currentState !is GalleryUiState.Success || currentState.photos.isEmpty()) {
            viewModel.loadFirstPage()
        }
    }

    // Пагинация при прокрутке
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                val totalItems = gridState.layoutInfo.totalItemsCount
                if (lastVisibleIndex != null && lastVisibleIndex >= totalItems - 3) {
                    viewModel.loadNextPage()
                }
            }
    }

    when (val state = uiState) {
        null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is GalleryUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is GalleryUiState.Success -> {
            Column(modifier = Modifier.fillMaxSize()) {
                PhotoGrid(
                    photos = state.photos,
                    gridState = gridState,
                    onPhotoClick = onPhotoClick,
                    modifier = Modifier.weight(1f)
                )
                if (state.isLoadingMore) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        is GalleryUiState.Error -> {
            if (state.isPaginationError) {
                // Ошибка пагинации - показываем список и ошибку внизу
                Column(modifier = Modifier.fillMaxSize()) {
                    PhotoGrid(
                        photos = state.photos,
                        gridState = gridState,
                        onPhotoClick = onPhotoClick,
                        modifier = Modifier.weight(1f)
                    )
                    PaginationErrorBar(
                        message = state.message,
                        onRetry = { viewModel.loadNextPage() }
                    )
                }
            } else {
                // Ошибка первой загрузки - показываем только ошибку
                ErrorScreen(
                    message = state.message,
                    onRetry = { viewModel.loadFirstPage() }
                )
            }
        }
    }
}

@Composable
private fun PhotoGrid(
    photos: List<Photo>,
    gridState: LazyGridState,
    onPhotoClick: (Photo) -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val spanCount = if (configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
        4
    } else {
        2
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(spanCount),
        state = gridState,
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        items(photos, key = { it.id }) { photo ->
            PhotoItem(
                photo = photo,
                onClick = { onPhotoClick(photo) }
            )
        }
    }
}

@Composable
private fun PhotoItem(
    photo: Photo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.aspectRatio(1f),
        shape = MaterialTheme.shapes.medium
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photo.thumbnailUrl)
                .size(Size(100, 100))
                .build(),
            contentDescription = photo.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun ErrorScreen(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
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

@Composable
private fun PaginationErrorBar(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onRetry) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}
