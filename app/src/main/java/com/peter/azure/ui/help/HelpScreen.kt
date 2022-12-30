package com.peter.azure.ui.help

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.peter.azure.data.entity.Help
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.ui.navigation.AzureTopBar
import com.peter.azure.ui.util.ErrorNotice
import com.peter.azure.ui.util.azureScreen

@Composable
fun HelpScreen(
    viewModel: HelpViewModel,
    navigateToMainScreens: (String) -> Unit
) {
    val navDialog = remember { mutableStateOf(false) }

    HelpContent(
        navDialogState = navDialog,
        navigateToMainScreens = navigateToMainScreens,
        uiState = viewModel.uiState.value
    )
}

@Composable
fun HelpContent(
    uiState: HelpUiState,
    navDialogState: MutableState<Boolean>,
    navigateToMainScreens: (String) -> Unit
) {

    Column(
        modifier = Modifier.azureScreen()
    ) {
        AzureTopBar(
            navDialogState = navDialogState,
            destination = AzureDestination.Main.HELP,
            navigateToMainScreens = navigateToMainScreens
        )
        Box(
            modifier = Modifier.weight(1f)
        ) {
            when (uiState) {
                is HelpUiState.Error -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        ErrorNotice(uiState.code)
                    }
                }
                is HelpUiState.Success -> {
                    HelpList(uiState.helpMap)
                }
                is HelpUiState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp, 24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HelpList(
    helpMap: Map<Help.Catalog, List<Help>>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        helpMap.forEach { (catalog, helpList) ->
            item {
                Column(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.medium
                        )
                ) {
                    Text(
                        text = catalog.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(
                            start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp
                        )
                    )
                    helpList.forEach { help ->
                        Column(
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 4.dp,
                                    bottom = 4.dp
                                )
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = help.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
                            )
                            Text(
                                text = help.text,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

//@Preview(name = "Help Screen", showBackground = true)
//@Preview(
//    name = "Help Screen (dark)", showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun HelpScreenPreview() {
//    val viewModel = HelpViewModel()
//    val navHostController = rememberNavController()
//    AzureTheme {
//        HelpScreen(viewModel, navHostController)
//    }
//}
