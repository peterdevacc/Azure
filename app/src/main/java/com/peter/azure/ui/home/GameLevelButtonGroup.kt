/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.home

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peter.azure.R
import com.peter.azure.data.entity.GameLevel
import com.peter.azure.ui.theme.AzureTheme


@Composable
fun GameLevelButtonGroup(
    isPortrait: Boolean,
    currentGameLevel: GameLevel,
    setGameLevel: (Int) -> Unit,
    startGame: () -> Unit,
) {
    val gameLevelTextList = stringArrayResource(R.array.game_level)
    val startButtonSize = 150 to 48

    if (isPortrait) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IncreaseGameLevelButton(
                currentGameLevel = currentGameLevel,
                setGameLevel = setGameLevel,
            )
            Spacer(modifier = Modifier.padding(4.dp))
            FilledTonalButton(
                onClick = startGame,
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.size(
                    startButtonSize.first.dp,
                    startButtonSize.second.dp
                ),
            ) {
                Text(
                    text = gameLevelTextList[currentGameLevel.num],
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            DecreaseGameLevelButton(
                currentGameLevel = currentGameLevel,
                setGameLevel = setGameLevel,
            )
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IncreaseGameLevelButton(
                currentGameLevel = currentGameLevel,
                setGameLevel = setGameLevel,
            )
            Spacer(modifier = Modifier.padding(4.dp))
            FilledTonalButton(
                onClick = startGame,
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.size(
                    startButtonSize.second.dp,
                    startButtonSize.first.dp
                ),
            ) {
                val gameLevelTextColor = MaterialTheme.colorScheme.onPrimaryContainer
                val gameLevelTextSize = startButtonSize.first * 0.43f
                val textWidthHalf = startButtonSize.second * 0.46f

                Canvas(
                    modifier = Modifier.size(
                        startButtonSize.second.dp,
                        (startButtonSize.first * 0.8f).toInt().dp
                    )
                ) {
                    drawIntoCanvas {
                        val path = android.graphics.Path().apply {
                            moveTo(size.width / 2f + textWidthHalf, size.height)
                            lineTo(size.width / 2f + textWidthHalf, 0f)
                        }
                        it.nativeCanvas.drawTextOnPath(
                            gameLevelTextList[currentGameLevel.num],
                            path,
                            0f,
                            0f,
                            Paint().apply {
                                textSize = gameLevelTextSize
                                textAlign = Paint.Align.CENTER
                                color = gameLevelTextColor.toArgb()
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
            DecreaseGameLevelButton(
                currentGameLevel = currentGameLevel,
                setGameLevel = setGameLevel,
            )
        }
    }

}

@Composable
private fun IncreaseGameLevelButton(
    currentGameLevel: GameLevel,
    setGameLevel: (Int) -> Unit,
) {
    SetLevelIconButton(
        enable = currentGameLevel != GameLevel.EASY,
        iconId = R.drawable.ic_arrow_left_24,
        iconCdId = R.string.icon_cd_increase_game_level,
        action = {
            if (currentGameLevel != GameLevel.EASY) {
                setGameLevel(currentGameLevel.num - 1)
            }
        }
    )
}

@Composable
private fun DecreaseGameLevelButton(
    currentGameLevel: GameLevel,
    setGameLevel: (Int) -> Unit,
) {
    SetLevelIconButton(
        enable = currentGameLevel != GameLevel.HARD,
        iconId = R.drawable.ic_arrow_right_24,
        iconCdId = R.string.icon_cd_decrease_game_level,
        action = {
            if (currentGameLevel != GameLevel.HARD) {
                setGameLevel(currentGameLevel.num + 1)
            }
        }
    )
}

@Composable
private fun SetLevelIconButton(
    enable: Boolean,
    iconId: Int,
    iconCdId: Int,
    action: () -> Unit,
) {
    FilledTonalButton(
        enabled = enable,
        onClick = action,
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(4.dp),
        modifier = Modifier.size(48.dp),
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = stringResource(iconCdId),
            modifier = Modifier.size(36.dp)
        )
    }
}

@Preview(
    name = "GameLevelButtonGroup",
    showBackground = true
)
//@Preview(
//    name = "GameLevelButtonGroup",
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true
//)
@Composable
fun GameLevelButtonGroupPreview() {
    AzureTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            GameLevelButtonGroup(
                isPortrait = false,
                setGameLevel = {},
                currentGameLevel = GameLevel.MODERATE,
                startGame = {},
            )
        }
    }
}
