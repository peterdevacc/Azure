package com.peter.azure.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peter.azure.R
import com.peter.azure.data.entity.Mark
import com.peter.azure.ui.theme.AzureTheme

@Composable
fun GameInput(
    blank: () -> Unit,
    mark: (Mark) -> Unit,
    write: (Int) -> Unit,
    submit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .border(1.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            ActionIcon(
                iconId = R.drawable.ic_clear_24,
                iconCd = "clean",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clickable(onClick = blank)
            )
            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            ActionIcon(
                iconId = R.drawable.ic_cross_24,
                iconCd = "mark wrong",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clickable{ mark(Mark.WRONG) }
            )
            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            ActionIcon(
                iconId = R.drawable.ic_circle_24,
                iconCd = "mark none",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clickable{ mark(Mark.NONE) }
            )
            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            ActionIcon(
                iconId = R.drawable.ic_submit_24,
                iconCd = "submit answer",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clickable(onClick = submit)
            )
        }
        Divider(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Column(
            modifier = Modifier.weight(4f)
        ) {
            val times = 3
            for (i in 0..2) {
                Row(modifier = Modifier.weight(1f)) {
                    for (j in 0..2) {
                        val num = (j + 1) + (i * times)
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { write(num) }
                            ) {
                                Text(
                                    text = "$num",
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                )
                            }
                        }
                        if (j != 2) {
                            Divider(
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp)
                            )
                        }
                    }
                }
                if (i != 2) {
                    Divider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionIcon(
    iconId: Int,
    iconCd: String,
    modifier: Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = iconCd,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(
    name = "GameInput",
    showBackground = true
)
//@Preview(
//    name = "GameInput", showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
@Composable
fun AboutScreenPreview() {
    AzureTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            GameInput({}, {}, {}, {})
        }
    }
}
