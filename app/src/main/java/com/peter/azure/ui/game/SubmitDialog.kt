package com.peter.azure.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.peter.azure.R

@Composable
fun SubmitDialog(
    isCorrect: Boolean,
    endGame: () -> Unit,
    onDismiss: () -> Unit
) {
    if (isCorrect) {
        val result = stringResource(R.string.screen_game_submit_dialog_correct_result)
        SubmitDialogContent(
            onDismiss = onDismiss,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        text = result,
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth()
                    )
                    Row {
                        Button(
                            onClick = onDismiss,
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                color = MaterialTheme.colorScheme.onError,
                                text = stringResource(R.string.screen_game_submit_dialog_cancel)
                            )
                        }
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        Button(
                            onClick = endGame,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                color = MaterialTheme.colorScheme.onPrimary,
                                text = stringResource(R.string.screen_game_submit_dialog_finish)
                            )
                        }
                    }
                }
            }
        }
    } else {
        val result = stringResource(R.string.screen_game_submit_dialog_wrong_result)
        SubmitDialogContent(
            onDismiss = onDismiss,
        ) {
            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = result,
                modifier = Modifier.padding(vertical = 12.dp),
            )
        }
    }
}

@Composable
fun SubmitDialogContent(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp)
        ) {
            Text(
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = stringResource(R.string.screen_game_submit_dialog_title),
            )
            content()
        }
    }
}
