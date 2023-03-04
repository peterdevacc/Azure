/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peter.azure.R
import com.peter.azure.data.entity.Location
import com.peter.azure.data.entity.Mark
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.ui.util.AzureTopBar
import com.peter.azure.ui.util.ErrorNotice
import com.peter.azure.ui.util.ProcessingDialog
import com.peter.azure.ui.util.azureScreen

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    isPortrait: Boolean,
    isCompact: Boolean,
    navigateUp: () -> Unit
) {
    val gameUiState by viewModel.gameUiState.collectAsStateWithLifecycle()
    GameContent(
        uiState = gameUiState,
        selectLocation = viewModel::selectLocation,
        writeNum = viewModel::writeNum,
        makeNote = viewModel::makeMark,
        blankLocation = viewModel::blankLocation,
        submitAnswer = viewModel::submitAnswer,
        requestDelete = viewModel::requestDelete,
        deleteGame = viewModel::deleteGame,
        closePlayingDialog = viewModel::closeDialog,
        isPortrait = isPortrait,
        isCompact = isCompact,
        navigateUp = navigateUp
    )
}

@Composable
private fun GameContent(
    uiState: GameUiState,
    deleteGame: () -> Unit,
    selectLocation: (Location, Int) -> Unit,
    writeNum: (Int) -> Unit,
    makeNote: (Mark) -> Unit,
    blankLocation: () -> Unit,
    submitAnswer: () -> Unit,
    requestDelete: () -> Unit,
    closePlayingDialog: () -> Unit,
    isPortrait: Boolean,
    isCompact: Boolean,
    navigateUp: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier.azureScreen()
    ) {
        val (topBar, gameContainer) = createRefs()

        val topBarModifier: Modifier
        val gameContainerModifier: Modifier

        if (isPortrait) {
            topBarModifier = Modifier.constrainAs(topBar) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
            gameContainerModifier = Modifier.constrainAs(gameContainer) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(topBar.bottom)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        } else {
            topBarModifier = Modifier.constrainAs(topBar) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }
            gameContainerModifier = Modifier
                .padding(start = 16.dp)
                .constrainAs(gameContainer) {
                    start.linkTo(topBar.end)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        }

        Box(modifier = topBarModifier) {
            AzureTopBar(
                isPortrait = isPortrait,
                destination = AzureDestination.General.GAME,
                navigateUp = navigateUp,
                leftSideContent = {
                    if (uiState is GameUiState.Playing) {
                        if (isPortrait) {
                            Row {
                                IconButton(onClick = requestDelete) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_delete_game_24),
                                        contentDescription = stringResource(R.string.icon_cd_delete_game),
                                        tint = MaterialTheme.colorScheme.onBackground,
                                    )
                                }
                                IconButton(onClick = submitAnswer) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_submit_24),
                                        contentDescription = stringResource(R.string.icon_cd_submit_answer),
                                        tint = MaterialTheme.colorScheme.onBackground,
                                    )
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                IconButton(onClick = requestDelete) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_delete_game_24),
                                        contentDescription = stringResource(R.string.icon_cd_delete_game),
                                        tint = MaterialTheme.colorScheme.onBackground,
                                    )
                                }
                                IconButton(onClick = submitAnswer) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_submit_24),
                                        contentDescription = stringResource(R.string.icon_cd_submit_answer),
                                        tint = MaterialTheme.colorScheme.onBackground,
                                    )
                                }
                            }
                        }

                    }
                }
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = gameContainerModifier
        ) {
            when (uiState) {
                is GameUiState.Playing -> {
                    GamePlayingContent(
                        uiState = uiState,
                        selectLocation = selectLocation,
                        writeNum = writeNum,
                        makeNote = makeNote,
                        blankLocation = blankLocation,
                        deleteGame = deleteGame,
                        closePlayingDialog = closePlayingDialog,
                        isPortrait = isPortrait,
                        isCompact = isCompact
                    )
                }
                is GameUiState.Error -> {
                    ErrorNotice(uiState.code)
                }
                is GameUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
                is GameUiState.GameEnded -> {
                    LaunchedEffect(Unit) {
                        navigateUp()
                    }
                }
            }
        }
    }
}

@Composable
private fun GamePlayingContent(
    uiState: GameUiState.Playing,
    selectLocation: (Location, Int) -> Unit,
    writeNum: (Int) -> Unit,
    makeNote: (Mark) -> Unit,
    blankLocation: () -> Unit,
    deleteGame: () -> Unit,
    closePlayingDialog: () -> Unit,
    isPortrait: Boolean,
    isCompact: Boolean
) {

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (board, markBlock, numPad, dialog) = createRefs()
        val portraitGuideline = Pair(
            createGuidelineFromStart(0.4f),
            createGuidelineFromBottom(0.3f)
        )
        val landscapeGuideline = Pair(
            createGuidelineFromEnd(0.3f),
            createGuidelineFromBottom(0.6f)
        )

        val boardModifier: Modifier
        val markBlockModifier: Modifier
        val numPadModifier: Modifier

        when (uiState.dialog) {
            GameUiState.Playing.Dialog.None -> Unit
            else -> {
                Box(
                    modifier = Modifier.constrainAs(dialog) {
                        centerHorizontallyTo(parent)
                        centerVerticallyTo(parent)
                    }
                ) {
                    when (uiState.dialog) {
                        GameUiState.Playing.Dialog.Submit -> {
                            ResultDialog(
                                isCorrect = uiState.isCorrect,
                                endGame = deleteGame,
                                onDismiss = closePlayingDialog
                            )
                        }
                        GameUiState.Playing.Dialog.Delete -> {
                            DeleteDialog(
                                delete = deleteGame,
                                onDismiss = closePlayingDialog
                            )
                        }
                        else -> {
                            ProcessingDialog()
                        }
                    }
                }
            }
        }

        if (isPortrait) {
            boardModifier = Modifier.constrainAs(board) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(portraitGuideline.second)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
            markBlockModifier = Modifier
                .padding(top = 16.dp)
                .constrainAs(markBlock) {
                    start.linkTo(parent.start)
                    end.linkTo(portraitGuideline.first)
                    top.linkTo(portraitGuideline.second)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            numPadModifier = Modifier
                .padding(top = 16.dp, start = 16.dp)
                .constrainAs(numPad) {
                    start.linkTo(portraitGuideline.first)
                    end.linkTo(parent.end)
                    top.linkTo(portraitGuideline.second)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        } else {
            boardModifier = Modifier
                .padding(end = 16.dp)
                .constrainAs(board) {
                    start.linkTo(parent.start)
                    end.linkTo(landscapeGuideline.first)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            markBlockModifier = Modifier.constrainAs(markBlock) {
                start.linkTo(landscapeGuideline.first)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(landscapeGuideline.second)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
            numPadModifier = Modifier
                .padding(top = 16.dp)
                .constrainAs(numPad) {
                    start.linkTo(landscapeGuideline.first)
                    end.linkTo(parent.end)
                    top.linkTo(landscapeGuideline.second)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        }

        Box(modifier = boardModifier) {
            GameBoard(
                puzzle = uiState.puzzle,
                selectedLocation = uiState.location,
                select = selectLocation,
                isCompact = isCompact
            )
        }

        Box(modifier = markBlockModifier) {
            GameMarkBlock(
                location = uiState.location,
                markList = uiState.markList,
                isCompact = isCompact
            )
        }

        Box(modifier = numPadModifier) {
            GamePad(
                blank = blankLocation,
                mark = makeNote,
                write = writeNum,
                isCompact = isCompact
            )
        }

    }

}
