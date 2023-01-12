/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.print

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
    val navDialogState = remember { mutableStateOf(false) }
    val isPortrait = LocalConfiguration.current.orientation ==
            Configuration.ORIENTATION_PORTRAIT

    PrintContent(
        pdfUiState = viewModel.pdfUiState.value,
        generatePdf = viewModel::generateSudokuPdf,
        notShareable = viewModel::notShareable,
        dismissDialog = viewModel::dismissDialog,
        gameLevelList = viewModel.gameLevelList.value,
        addGameLevel = viewModel::addGameLevel,
        removeGameLevel = viewModel::removeGameLevel,
        isPortrait = isPortrait,
        navDialogState = navDialogState,
        navigateToMainScreens = navigateToMainScreens
    )
}

@Composable
fun PrintContent(
    pdfUiState: PdfUiState,
    generatePdf: () -> Unit,
    notShareable: () -> Unit,
    dismissDialog: () -> Unit,
    gameLevelList: List<GameLevel>,
    addGameLevel: (GameLevel) -> Unit,
    removeGameLevel: (GameLevel) -> Unit,
    isPortrait: Boolean,
    navDialogState: MutableState<Boolean>,
    navigateToMainScreens: (String) -> Unit
) {
    val context = LocalContext.current

    ConstraintLayout(
        modifier = Modifier.azureScreen()
    ) {
        val (
            topBar, pdf, levelList, levelButtonList,
            shareButton, generateButton
        ) = createRefs()

        val horizontalGuideline = createGuidelineFromEnd(0.18f)
        val centerHorizontalGuideline = createGuidelineFromStart(0.5f)

        val topBarModifier: Modifier
        val pdfModifier: Modifier
        val levelListModifier: Modifier
        val levelButtonListModifier: Modifier
        val shareButtonModifier: Modifier
        val generateButtonModifier: Modifier

        if (isPortrait) {
            topBarModifier = Modifier.constrainAs(topBar) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
            pdfModifier = Modifier.constrainAs(pdf) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(topBar.bottom)
                bottom.linkTo(levelList.top)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
            levelListModifier = Modifier
                .padding(top = 8.dp)
                .constrainAs(levelList) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(levelButtonList.top)
                    width = Dimension.fillToConstraints
                }
            levelButtonListModifier = Modifier.constrainAs(levelButtonList) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(shareButton.top)
                width = Dimension.fillToConstraints
            }
            shareButtonModifier = Modifier
                .padding(end = 4.dp, top = 8.dp)
                .height(36.dp)
                .constrainAs(shareButton) {
                    start.linkTo(parent.start)
                    end.linkTo(centerHorizontalGuideline)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
            generateButtonModifier = Modifier
                .padding(start = 4.dp, top = 8.dp)
                .height(36.dp)
                .constrainAs(generateButton) {
                    start.linkTo(centerHorizontalGuideline)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
        } else {
            topBarModifier = Modifier.constrainAs(topBar) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }
            pdfModifier = Modifier
                .padding(horizontal = 16.dp)
                .constrainAs(pdf) {
                    start.linkTo(topBar.end)
                    end.linkTo(horizontalGuideline)
                    top.linkTo(parent.top)
                    bottom.linkTo(levelList.top)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            levelListModifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .constrainAs(levelList) {
                    start.linkTo(topBar.end)
                    end.linkTo(horizontalGuideline)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
            levelButtonListModifier = Modifier.constrainAs(levelButtonList) {
                start.linkTo(horizontalGuideline)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(shareButton.top)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
            shareButtonModifier = Modifier
                .padding(bottom = 4.dp)
                .height(36.dp)
                .constrainAs(shareButton) {
                    start.linkTo(horizontalGuideline)
                    end.linkTo(parent.end)
                    bottom.linkTo(generateButton.top)
                    width = Dimension.fillToConstraints
                }
            generateButtonModifier = Modifier
                .padding(top = 4.dp)
                .height(36.dp)
                .constrainAs(generateButton) {
                    start.linkTo(horizontalGuideline)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
        }

        Box(modifier = topBarModifier) {
            AzureTopBar(
                isPortrait = isPortrait,
                navDialogState = navDialogState,
                destination = AzureDestination.Main.PRINT,
                navigateToMainScreens = navigateToMainScreens
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = pdfModifier
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

        Box(modifier = levelListModifier) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.primaryContainer)
            ) {
                LazyRow(
                    verticalAlignment = Alignment.CenterVertically,
                    contentPadding = PaddingValues(horizontal = 12.dp),
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
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            border = BorderStroke(
                                1.dp, MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier
                                .padding(end = 8.dp)
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
                    color = MaterialTheme.colorScheme.primaryContainer,
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
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Column(modifier = levelButtonListModifier) {
            if (isPortrait) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 8.dp)
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
                                .padding(end = 8.dp)
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
            } else {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(GameLevel.values()) { level ->
                        OutlinedButton(
                            onClick = { addGameLevel(level) },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            shape = MaterialTheme.shapes.medium,
                            border = BorderStroke(
                                1.dp, MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .fillMaxWidth()
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
            }
        }

        Button(
            onClick = {
                if (pdfUiState is PdfUiState.Loaded) {
                    val file = pdfUiState.sudokuPdf.file
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.putExtra(Intent.EXTRA_STREAM, file)
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
                    notShareable()
                }
            },
            shape = MaterialTheme.shapes.medium,
            modifier = shareButtonModifier
        ) {
            Text(stringResource(R.string.screen_print_share_pdf))
        }

        Button(
            onClick = generatePdf,
            shape = MaterialTheme.shapes.medium,
            modifier = generateButtonModifier
        ) {
            Text(stringResource(R.string.screen_print_generate_pdf))
        }

//        Box(modifier = shareButtonModifier) {
//            if (isPortrait) {
//                Row(
//                    modifier = Modifier
//                        .padding(top = 8.dp)
//                        .fillMaxWidth()
//                ) {
//                    Button(
//                        onClick = {
//                            if (pdfUiState is PdfUiState.Loaded) {
//                                val file = pdfUiState.sudokuPdf.file
//                                val shareIntent = Intent(Intent.ACTION_SEND)
//                                shareIntent.putExtra(Intent.EXTRA_STREAM, file)
//                                shareIntent.putExtra(
//                                    Intent.EXTRA_STREAM,
//                                    FileProvider.getUriForFile(
//                                        context,
//                                        "com.peter.azure.file_provider",
//                                        file
//                                    )
//                                )
//                                shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//                                shareIntent.type = "application/pdf"
//                                startActivity(context, shareIntent, null)
//                            } else {
//                                notShareable()
//                            }
//                        },
//                        shape = MaterialTheme.shapes.medium,
//                        modifier = Modifier.weight(1f).height(36.dp)
//                    ) {
//                        Text(stringResource(R.string.screen_print_share_pdf))
//                    }
//                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
//                    Button(
//                        onClick = generatePdf,
//                        shape = MaterialTheme.shapes.medium,
//                        modifier = Modifier.weight(1f).height(36.dp)
//                    ) {
//                        Text(stringResource(R.string.screen_print_generate_pdf))
//                    }
//                }
//            } else {
//                Column(
//                    modifier = Modifier
//                        .padding(top = 4.dp)
//                        .fillMaxWidth()
//                ) {
//                    Button(
//                        onClick = generatePdf,
//                        shape = MaterialTheme.shapes.medium,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(36.dp)
//                    ) {
//                        Text(stringResource(R.string.screen_print_generate_pdf))
//                    }
//                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
//                    Button(
//                        onClick = {
//                            if (pdfUiState is PdfUiState.Loaded) {
//                                val file = pdfUiState.sudokuPdf.file
//                                val shareIntent = Intent(Intent.ACTION_SEND)
//                                shareIntent.putExtra(Intent.EXTRA_STREAM, file)
//                                shareIntent.putExtra(
//                                    Intent.EXTRA_STREAM,
//                                    FileProvider.getUriForFile(
//                                        context,
//                                        "com.peter.azure.file_provider",
//                                        file
//                                    )
//                                )
//                                shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//                                shareIntent.type = "application/pdf"
//                                startActivity(context, shareIntent, null)
//                            } else {
//                                notShareable()
//                            }
//                        },
//                        shape = MaterialTheme.shapes.medium,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(36.dp)
//                    ) {
//                        Text(stringResource(R.string.screen_print_share_pdf))
//                    }
//                }
//            }
//        }
    }

}

//@Preview(name = "Print Screen", showBackground = true)
//@Preview(
//    name = "Print Screen", showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun PrintScreenPreview() {
//    val navDialog = remember { mutableStateOf(false) }
//
//    AzureTheme {
//        PrintContent(
//            navDialog, PdfUiState.Default,
//            {}, {}, {}, emptyList(), {}, {}, {}
//        )
//    }
//}
