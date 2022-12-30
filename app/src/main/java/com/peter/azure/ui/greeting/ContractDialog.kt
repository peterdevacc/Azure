package com.peter.azure.ui.greeting

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.peter.azure.data.entity.Info
import com.peter.azure.ui.util.InfoDocument

@Composable
fun ContractDialog(
    info: Info,
    onDismiss: () -> Unit
) {
    val infoTitle = when (info.type) {
        Info.Type.SERVICE -> "Terms of service"
        Info.Type.PRIVACY -> "Privacy policy"
        Info.Type.ACKNOWLEDGEMENTS -> "Acknowledgements"
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            Column {
                Text(
                    text = infoTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(
                        start = 12.dp, end = 12.dp,
                        top = 12.dp, bottom = 4.dp
                    )
                )
                InfoDocument(
                    info = info,
                    modifier = Modifier.weight(1f)
                )
                Spacer(
                    modifier = Modifier.padding(
                        start = 12.dp, end = 12.dp,
                        top = 4.dp, bottom = 12.dp
                    )
                )
            }
        }
    }
}

