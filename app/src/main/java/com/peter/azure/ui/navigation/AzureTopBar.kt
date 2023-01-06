package com.peter.azure.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.peter.azure.R
import com.peter.azure.ui.theme.azureShapes

@Composable
fun AzureTopBar(
    destination: AzureDestination.Main,
    navDialogState: MutableState<Boolean>,
    navigateToMainScreens: (String) -> Unit,
) {

    if (navDialogState.value) {
        AzureNavigationDialog(
            currentMainDestination = destination,
            navigateToMainScreens = navigateToMainScreens,
            onDismiss = { navDialogState.value = false }
        )
    }

    ConstraintLayout(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 22.dp)
            .fillMaxWidth()

    ) {
        val (icon, heading) = createRefs()
        Text(
            text = stringResource(destination.textId),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.constrainAs(heading) {
                start.linkTo(parent.start)
                centerVerticallyTo(parent)
            }
        )
        FilledTonalIconButton(
            onClick = { navDialogState.value = true },
            shape = azureShapes.large,
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ),
            modifier = Modifier
                .size(48.dp)
                .constrainAs(icon) {
                    end.linkTo(parent.end)
                    centerVerticallyTo(parent)
                },
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_menu_24),
                contentDescription = stringResource(R.string.icon_cd_navigation_menu),
                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.size(24.dp)
            )
        }
    }

}

@Composable
fun AzureTopBar(
    destination: AzureDestination.General,
    navigateUp: () -> Unit,
    leftSideContent: @Composable () -> Unit = {}
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .fillMaxWidth()
    ) {
        val (icon, heading, leftSide) = createRefs()
        IconButton(
            onClick = navigateUp,
            modifier = Modifier.constrainAs(icon) {
                start.linkTo(parent.start)
                centerVerticallyTo(parent)
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back_24),
                contentDescription = stringResource(R.string.icon_cd_navigate_up),
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        Text(
            text = stringResource(destination.textId),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.constrainAs(heading) {
                centerHorizontallyTo(parent)
                centerVerticallyTo(parent)
            }
        )
        Box(
            modifier = Modifier.constrainAs(leftSide) {
                end.linkTo(parent.end)
                centerVerticallyTo(parent)
            },
        ) {
            leftSideContent()
        }
    }
}
