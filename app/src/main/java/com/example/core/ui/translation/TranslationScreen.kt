package com.example.core.ui.translation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.core.R
import com.example.core.di.AppModule
import com.example.core.ui.ViewModelFactory
import com.example.core.data.Languages

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationScreen(
    viewModel: TranslationViewModel = viewModel(
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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Переводчик",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "С",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                var expandedSource by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedSource,
                    onExpandedChange = { expandedSource = !expandedSource }
                ) {
                    OutlinedTextField(
                        value = Languages.getLanguageName(uiState.sourceLanguage),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSource) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 12.sp),
                        singleLine = true

                    )
                    ExposedDropdownMenu(
                        expanded = expandedSource,
                        onDismissRequest = { expandedSource = false }
                    ) {
                        Languages.supportedLanguages.forEach { language ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        language.name,
                                        fontSize = 12.sp
                                    )
                                },
                                onClick = {
                                    viewModel.setSourceLanguage(language.code)
                                    expandedSource = false
                                }
                            )
                        }
                    }
                }
            }

            IconButton(
                onClick = {
                    val temp = uiState.sourceLanguage
                    viewModel.setSourceLanguage(uiState.targetLanguage)
                    viewModel.setTargetLanguage(temp)
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .offset(y=12.dp)
            ) {
                Icon(
                    painterResource(R.drawable.change_arrow),
                    contentDescription = "Поменять языки"
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "На",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                var expandedTarget by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedTarget,
                    onExpandedChange = { expandedTarget = !expandedTarget }
                ) {
                    OutlinedTextField(
                        value = Languages.getLanguageName(uiState.targetLanguage),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTarget) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 12.sp),
                        singleLine = true

                    )
                    ExposedDropdownMenu(
                        expanded = expandedTarget,
                        onDismissRequest = { expandedTarget = false }
                    ) {
                        Languages.supportedLanguages.forEach { language ->
                            DropdownMenuItem(
                                text = { Text(language.name) },
                                onClick = {
                                    viewModel.setTargetLanguage(language.code)
                                    expandedTarget = false
                                }
                            )
                        }
                    }
                }
            }
        }

        OutlinedTextField(
            value = uiState.sourceText,
            onValueChange = { viewModel.setSourceText(it) },
            label = { Text("Введите текст для перевода") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        Button(
            onClick = { viewModel.translate() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading && uiState.sourceText.isNotBlank()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text("Перевести")
        }

        if (uiState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        OutlinedTextField(
            value = uiState.translatedText,
            onValueChange = {},
            label = { Text("Перевод") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            readOnly = true,
            minLines = 3,
            maxLines = 10
        )

        uiState.errorMessage?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { viewModel.clearError() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрыть"
                        )
                    }
                }
            }
        }
    }
}

