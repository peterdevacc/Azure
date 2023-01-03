package com.peter.azure.ui.contract

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.ui.navigation.AzureTopBar
import com.peter.azure.ui.util.ErrorNotice
import com.peter.azure.ui.util.InfoDocument
import com.peter.azure.ui.util.azureScreen

@Composable
fun ContractScreen(
    viewModel: ContractViewModel,
    navigateUp: () -> Unit
) {

    ContractContent(
        uiState = viewModel.uiState.value,
        navigateUp = navigateUp
    )

}

@Composable
fun ContractContent(
    uiState: ContractUiState,
    navigateUp: () -> Unit
) {
    Column(
        modifier = Modifier.azureScreen()
    ) {
        AzureTopBar(
            destination = AzureDestination.General.CONTRACT,
            navigateUp = navigateUp
        )
        Box(
            modifier = Modifier.weight(1f)
        ) {
            when (uiState) {
                is ContractUiState.Error -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        ErrorNotice(uiState.code)
                    }
                }
                is ContractUiState.Success -> {
                    InfoDocument(
                        info = uiState.info,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.large)
                    )
                }
                is ContractUiState.Loading -> {
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

//@Preview(name = "Contract Screen", showBackground = true)
//@Preview(
//    name = "Contract Screen", showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun ContractScreenPreview() {
//    AzureTheme {
//
//    }
//}
