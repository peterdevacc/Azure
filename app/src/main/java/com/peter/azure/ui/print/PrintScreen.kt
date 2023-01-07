package com.peter.azure.ui.print

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.peter.azure.R
import com.peter.azure.data.entity.GameLevel
import com.peter.azure.data.util.PDF_PAGE_SIZE
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.ui.navigation.AzureTopBar
import com.peter.azure.ui.util.ErrorDialog
import com.peter.azure.ui.util.ProcessingDialog
import com.peter.azure.ui.util.azureScreen

@Composable
fun PrintScreen(
    viewModel: PrintViewModel,
    navigateToMainScreens: (String) -> Unit
) {
    val navDialog = remember { mutableStateOf(false) }

    PrintContent(
        navDialogState = navDialog,
        pdfUiState = viewModel.pdfUiState.value,
        generatePdf = viewModel::generateSudokuPdf,
        notShareableNotice = viewModel::notShareableDialog,
        dismissDialog = viewModel::dismissDialog,
        gameLevelList = viewModel.gameLevelList.value,
        addGameLevel = viewModel::addGameLevel,
        removeGameLevel = viewModel::removeGameLevel,
        navigateToMainScreens = navigateToMainScreens
    )
}

@Composable
fun PrintContent(
    navDialogState: MutableState<Boolean>,
    pdfUiState: PdfUiState,
    generatePdf: () -> Unit,
    notShareableNotice: () -> Unit,
    dismissDialog: () -> Unit,
    gameLevelList: List<GameLevel>,
    addGameLevel: (GameLevel) -> Unit,
    removeGameLevel: (GameLevel) -> Unit,
    navigateToMainScreens: (String) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.azureScreen()
    ) {
        AzureTopBar(
            navDialogState = navDialogState,
            destination = AzureDestination.Main.PRINT,
            navigateToMainScreens = navigateToMainScreens
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                when (pdfUiState) {
                    PdfUiState.Default -> {
                        Icon(
                            painter = painterResource(R.drawable.ic_print_nav_24),
                            contentDescription = stringResource(R.string.icon_cd_screen_print),
                            tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                            modifier = Modifier.size(128.dp)
                        )
                    }
                    is PdfUiState.Loaded -> {
                        PdfViewer(
                            preview = pdfUiState.sudokuPdf.preview,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    is PdfUiState.EmptyGameLevelList -> {
                        PrintMessageDialog(
                            title = stringResource(
                                R.string.screen_print_empty_game_level_list_dialog_title
                            ),
                            text = stringResource(
                                R.string.screen_print_empty_game_level_list_dialog_msg
                            ),
                            onDismiss = dismissDialog
                        )
                    }
                    is PdfUiState.NotShareable -> {
                        PrintMessageDialog(
                            title = stringResource(
                                R.string.screen_print_not_shareable_pdf_dialog_title
                            ),
                            text = stringResource(
                                R.string.screen_print_not_shareable_pdf_dialog_msg
                            ),
                            onDismiss = dismissDialog
                        )
                    }
                    PdfUiState.Processing -> {
                        ProcessingDialog()
                    }
                    is PdfUiState.Error -> {
                        ErrorDialog(
                            code = pdfUiState.code,
                            onDismiss = dismissDialog
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.secondaryContainer)
            ) {
                LazyRow(
                    verticalAlignment = Alignment.CenterVertically,
                    contentPadding = PaddingValues(4.dp),
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .weight(1f)
                        .height(40.dp)
                ) {
                    items(gameLevelList) { level ->
                        OutlinedButton(
                            onClick = { removeGameLevel(level) },
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.secondary
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            border = BorderStroke(
                                1.dp, MaterialTheme.colorScheme.secondaryContainer
                            ),
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(28.dp)
                        ) {
                            Text(
                                text = level.name,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_clear_24),
                                contentDescription = "",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                Divider(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(40.dp)
                        .width(1.dp)
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(40.dp)
                ) {
                    Text(
                        text = "${gameLevelList.size} / $PDF_PAGE_SIZE",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 4.dp)
                    .fillMaxWidth()
            ) {
                GameLevel.values().forEach { level ->
                    OutlinedButton(
                        onClick = { addGameLevel(level) },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(
                            1.dp, MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(28.dp)
                    ) {
                        Text(
                            text = level.name,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_add_24),
                            contentDescription = "",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (pdfUiState is PdfUiState.Loaded) {
                            val file = pdfUiState.sudokuPdf.file
                            val shareIntent = Intent(Intent.ACTION_SEND)
                            shareIntent.putExtra(Intent.EXTRA_STREAM,  file)
                            shareIntent.putExtra(
                                Intent.EXTRA_STREAM,
                                FileProvider.getUriForFile(
                                    context,
                                    "com.peter.azure.file_provider",
                                    file
                                )
                            )
                            shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            shareIntent.type = "application/pdf"
                            startActivity(context, shareIntent, null)
                        } else {
                            notShareableNotice()
                        }
                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.screen_print_share_pdf))
                }
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Button(
                    onClick = generatePdf,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.screen_print_generate_pdf))
                }
            }
        }
    }
}

//@Preview(name = "Print Screen", showBackground = true)
//@Preview(
//    name = "Print Screen", showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun PrintScreenPreview() {
//    AzureTheme {
//        PrintContent(false, {}, {}, {})
//    }
//}
