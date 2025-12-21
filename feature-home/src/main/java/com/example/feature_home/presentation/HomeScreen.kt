package com.example.feature_home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.feature_home.presentation.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    var textField1 by remember { mutableStateOf("test") }


    val trackState = viewModel.trackState.collectAsState()
    val secondState = viewModel.someBodyState.collectAsState()


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = textField1,
            onValueChange = { textField1 = it },
            label = { Text("Поисковый запрос") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = if (trackState.value.isEmpty()) "Количество треков будет показано здесь" else trackState.value,
            onValueChange = { },
            label = { Text("Количество найденных треков") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            readOnly = true
        )

        OutlinedTextField(
            value = if (secondState.value.isEmpty()) "Количество результатов будет показано здесь" else secondState.value,
            onValueChange = { },
            label = { Text("Количество найденных результатов") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            readOnly = true
        )

        Button(
            onClick = {
                viewModel.getTracks(textField1)
                viewModel.getSomeBody(textField1)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Отправить")
        }
    }
}