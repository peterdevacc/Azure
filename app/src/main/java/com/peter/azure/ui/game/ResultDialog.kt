/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

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
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.peter.azure.R

@Composable
fun ResultDialog(
    isCorrect: Boolean,
    endGame: () -> Unit,
    onDismiss: () -> Unit
) {
    if (isCorrect) {
        CorrectContent(
            endGame = endGame,
            onDismiss = onDismiss
        )
    } else {
        WrongContent(
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun CorrectContent(
    onDismiss: () -> Unit,
    endGame: () -> Unit,
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
                .semantics {
                    liveRegion = LiveRegionMode.Assertive
                }
        ) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = stringResource(R.string.screen_game_result_dialog_title),
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        text = stringResource(R.string.screen_game_result_dialog_correct),
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
                                text = stringResource(R.string.screen_game_result_dialog_cancel)
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
                                text = stringResource(R.string.screen_game_result_dialog_finish)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WrongContent(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp)
                .fillMaxWidth()
                .semantics {
                    liveRegion = LiveRegionMode.Assertive
                }
        ) {
            Text(
                text = stringResource(R.string.screen_game_result_dialog_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(
                text = stringResource(R.string.screen_game_result_dialog_wrong),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Button(
                onClick = onDismiss,
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.screen_game_result_dialog_ok),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


