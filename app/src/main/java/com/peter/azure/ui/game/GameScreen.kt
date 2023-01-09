package com.peter.azure.ui.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peter.azure.R
import com.peter.azure.data.entity.Location
import com.peter.azure.data.entity.Mark
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.ui.navigation.AzureTopBar
import com.peter.azure.ui.util.ErrorNotice
import com.peter.azure.ui.util.ProcessingDialog
import com.peter.azure.ui.util.azureScreen

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    navigateUp: () -> Unit
) {

    GameContent(
        uiState = viewModel.gameUiState.value,
        selectLocation = viewModel::selectLocation,
        writeNum = viewModel::writeNum,
        makeNote = viewModel::makeNote,
        blankLocation = viewModel::blankLocation,
        submitAnswer = viewModel::submitAnswer,
        requestDelete = viewModel::requestDelete,
        deleteGame = viewModel::deleteGame,
        closePlayingDialog = viewModel::closeDialog,
        navigateUp = navigateUp
    )

}

@Composable
fun GameContent(
    uiState: GameUiState,
    deleteGame:() -> Unit,
    selectLocation: (Location, Int) -> Unit,
    writeNum: (Int) -> Unit,
    makeNote: (Mark) -> Unit,
    blankLocation: () -> Unit,
    submitAnswer: () -> Unit,
    requestDelete: () -> Unit,
    closePlayingDialog: () -> Unit,
    navigateUp: () -> Unit
) {

    when (uiState) {
        is GameUiState.Error -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.azureScreen()
            ) {
                ErrorNotice(uiState.code)
            }
        }
        GameUiState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.azureScreen()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp, 24.dp)
                )
            }
        }
        is GameUiState.Playing -> {
            GamePlayingContent(
                uiState = uiState,
                selectLocation = selectLocation,
                writeNum = writeNum,
                makeNote = makeNote,
                blankLocation = blankLocation,
                submitAnswer = submitAnswer,
                requestDelete = requestDelete,
                deleteGame = deleteGame,
                closePlayingDialog = closePlayingDialog,
                navigateUp = navigateUp
            )
        }
        GameUiState.GameEnded -> {
            LaunchedEffect(Unit) {
                navigateUp()
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
    submitAnswer: () -> Unit,
    requestDelete: () -> Unit,
    deleteGame:() -> Unit,
    closePlayingDialog: () -> Unit,
    navigateUp: () -> Unit
) {
    when (uiState.dialog) {
        GameUiState.Playing.Dialog.Submit -> {
            SubmitDialog(
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
        GameUiState.Playing.Dialog.Processing -> {
            ProcessingDialog()
        }
        GameUiState.Playing.Dialog.None -> Unit
    }

    Column(
        modifier = Modifier.azureScreen()
    ) {
        AzureTopBar(
            destination = AzureDestination.General.GAME,
            navigateUp = navigateUp,
            leftSideContent = {
                Row {
                    IconButton(
                        onClick = requestDelete
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete_game_24),
                            contentDescription = stringResource(R.string.icon_cd_delete_game),
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                    IconButton(
                        onClick = submitAnswer
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_submit_24),
                            contentDescription = stringResource(R.string.icon_cd_submit_answer),
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
            }
        )
        Column(
            modifier = Modifier.weight(6.3f)
        ) {
            GameBoard(
                puzzle = uiState.puzzle,
                selectedLocation = uiState.location,
                selectLocation = selectLocation
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .weight(2.7f)
        ) {
            Box(
                modifier = Modifier.weight(2f)
            ) {
                GameMarkBlock(uiState.markList)
            }
            Spacer(modifier = Modifier.padding(horizontal = 6.dp))
            Box(
                modifier = Modifier.weight(3f)
            ) {
                GamePad(
                    blank = blankLocation,
                    mark = makeNote,
                    write = writeNum,
                )
            }
        }
    }
}

//@Preview(
//    name = "GameScreen",
//    showBackground = true
//)
//@Preview(
//    name = "GameScreen", showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun GameScreenPreview() {
//    AzureTheme {
//    }
//}
