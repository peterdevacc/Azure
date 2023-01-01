package com.peter.azure.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.peter.azure.ui.theme.AzureTheme

@Composable
fun AzureNavigationDialog(
    currentMainDestination: AzureDestination.Main,
    navigateToMainScreens: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val mainDestinationList = AzureDestination
        .Main
        .getNavigationList(currentMainDestination)

    Dialog(
        onDismissRequest = onDismiss
    ) {
        LazyColumn(
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color.Transparent)
        ) {
            items(mainDestinationList) { destination ->
                Surface(
                    color = MaterialTheme.colorScheme.surface
                        .copy(alpha = 0.8f),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .height(108.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            onDismiss()
                            navigateToMainScreens(destination.route)
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Image(
                                painter = painterResource(destination.imageId),
                                contentDescription = stringResource(
                                    destination.imageContentDescriptionId
                                ),
                                modifier = Modifier
                                    .padding(bottom = 2.dp)
                                    .weight(1f)
                                    .align(Alignment.CenterHorizontally),
                                contentScale = ContentScale.FillBounds
                            )
                            Text(
                                text = stringResource(destination.textId),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .weight(2.5f)
                        ) {
                            Text(
                                text = stringResource(destination.descriptionId),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AzureNavigationDialogPreview() {
    AzureTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AzureNavigationDialog(AzureDestination.Main.ABOUT, {}, {})
        }
    }
}
