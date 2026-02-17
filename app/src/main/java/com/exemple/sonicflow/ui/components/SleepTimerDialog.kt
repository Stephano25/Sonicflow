package com.exemple.sonicflow.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.*

@Composable
fun SleepTimerDialog(
    onDismiss: () -> Unit,
    onTimerFinished: () -> Unit
) {

    var minutes by remember { mutableStateOf(10f) }
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                scope.launch {
                    delay((minutes * 60 * 1000).toLong())
                    onTimerFinished()
                }
                onDismiss()
            }) {
                Text("Start")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Sleep Timer") },
        text = {
            Slider(
                value = minutes,
                onValueChange = { minutes = it },
                valueRange = 5f..60f
            )
        }
    )
}
