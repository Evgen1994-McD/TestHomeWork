package com.example.feature_home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.home.presentation.HomeViewModel

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
            value = when{
                trackState.value.toString().isEmpty() ->"Что то не так"
                else -> {trackState.value.toString()}
            },
            onValueChange = { textField1 = it },
            label = { Text("Первое поле") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = when{
                secondState.value.toString().isEmpty() ->"Что то не так"
                else -> {secondState.value.toString()}
            },
            onValueChange = { textField1 = it },
            label = { Text("Второе поле") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = {
                // Здесь можно вызвать метод из ViewModel
                viewModel.getTracks(textField1)
                viewModel.getSomeBody(textField1)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Отправить")
        }
    }
}