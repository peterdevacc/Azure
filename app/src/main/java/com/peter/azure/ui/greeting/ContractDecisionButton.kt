package com.peter.azure.ui.greeting

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.azure.ui.theme.AzureTheme

@Composable
fun ContractDecisionButton(
    text: String,
    decision: () -> Unit
) {
    Button(
        onClick = decision,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(54.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text.uppercase(),
            fontSize = 20.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingDecisionButtonPreview() {
    AzureTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            ContractDecisionButton("Continue") {}
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}