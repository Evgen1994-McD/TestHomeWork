package com.example.core.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.DismissDirection
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.data.Languages
import com.example.core.domain.repository.SortType
import com.example.core.di.AppModule
import com.example.core.domain.model.Translation
import com.example.core.ui.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = viewModel(
        factory = ViewModelFactory(
            translateUseCase = AppModule.getTranslateUseCase(LocalContext.current),
            getTranslationHistoryUseCase = AppModule.getTranslationHistoryUseCase(LocalContext.current),
            deleteTranslationUseCase = AppModule.getDeleteTranslationUseCase(LocalContext.current)
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "История переводов",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    var expandedSource by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedSource,
                        onExpandedChange = { expandedSource = !expandedSource },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = uiState.selectedSourceLanguage?.let {
                                Languages.getLanguageName(
                                    it
                                )
                            } ?: "Все языки (от)",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Язык источника") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSource) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedSource,
                            onDismissRequest = { expandedSource = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Все языки") },
                                onClick = {
                                    viewModel.setSourceLanguageFilter(null)
                                    expandedSource = false
                                }
                            )
                            Languages.supportedLanguages.forEach { language ->
                                DropdownMenuItem(
                                    text = { Text(language.name) },
                                    onClick = {
                                        viewModel.setSourceLanguageFilter(language.code)
                                        expandedSource = false
                                    }
                                )
                            }
                        }
                    }

                    var expandedTarget by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedTarget,
                        onExpandedChange = { expandedTarget = !expandedTarget },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = uiState.selectedTargetLanguage?.let {
                                Languages.getLanguageName(
                                    it
                                )
                            } ?: "Все языки (на)",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Язык назначения") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTarget) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedTarget,
                            onDismissRequest = { expandedTarget = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Все языки") },
                                onClick = {
                                    viewModel.setTargetLanguageFilter(null)
                                    expandedTarget = false
                                }
                            )
                            Languages.supportedLanguages.forEach { language ->
                                DropdownMenuItem(
                                    text = { Text(language.name) },
                                    onClick = {
                                        viewModel.setTargetLanguageFilter(language.code)
                                        expandedTarget = false
                                    }
                                )
                            }
                        }
                    }
                }

                var expandedSort by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedSort,
                    onExpandedChange = { expandedSort = !expandedSort }
                ) {
                    OutlinedTextField(
                        value = getSortTypeName(uiState.sortType),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Сортировка") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSort) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedSort,
                        onDismissRequest = { expandedSort = false }
                    ) {
                        SortType.values().forEach { sortType ->
                            DropdownMenuItem(
                                text = { Text(getSortTypeName(sortType)) },
                                onClick = {
                                    viewModel.setSortType(sortType)
                                    expandedSort = false
                                }
                            )
                        }
                    }
                }

                if (uiState.selectedSourceLanguage != null || uiState.selectedTargetLanguage != null) {
                    TextButton(
                        onClick = { viewModel.clearFilters() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Сбросить фильтры")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.filteredTranslations.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет переводов",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = uiState.filteredTranslations,
                    key = { it.id }
                ) { translation ->
                    SwipeToDeleteTranslationItem(
                        translation = translation,
                        onDelete = { viewModel.deleteTranslation(translation) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToDeleteTranslationItem(
    translation: Translation,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberDismissState()
    val dismissDirection = DismissDirection.EndToStart

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == DismissValue.DismissedToStart) {
            onDelete()
        }
    }

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(dismissDirection),
        background = {
            val color = when (dismissState.targetValue) {
                DismissValue.DismissedToStart -> Color.Red
                else -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить",
                    tint = Color.White
                )
            }
        },
        dismissContent = {
            TranslationItemCard(translation = translation)
        },
        modifier = modifier
    )
}

@Composable
fun TranslationItemCard(translation: Translation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Языки
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${Languages.getLanguageName(translation.sourceLanguage)} → ${
                        Languages.getLanguageName(
                            translation.targetLanguage
                        )
                    }",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = formatTimestamp(translation.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Divider()

            Text(
                text = translation.sourceText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = translation.translatedText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun getSortTypeName(sortType: SortType): String {
    return when (sortType) {
        SortType.DATE_DESC -> "По дате (новые)"
        SortType.DATE_ASC -> "По дате (старые)"
        SortType.TEXT_ASC -> "По тексту (А-Я)"
        SortType.TEXT_DESC -> "По тексту (Я-А)"
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

