package com.peter.azure.ui.game

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peter.azure.data.entity.Cell
import com.peter.azure.data.entity.Location
import com.peter.azure.data.entity.Puzzle

@Composable
fun GameBoard(
    puzzle: Puzzle,
    selectedLocation: Location,
    selectLocation: (Location, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .border(1.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        for (i in 0..8) {
            Row(modifier = Modifier.weight(1f)) {
                for (j in 0..8) {
                    val cell = puzzle.getCell(i, j)
                    GameCell(
                        cell = cell,
                        location = Location(i, j),
                        selectedLocation = selectedLocation,
                        select = selectLocation,
                        modifier = Modifier.weight(1f)
                    )
                    if (j != 8) {
                        Divider(
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                        )
                    }
                }
            }
            if (i != 8) {
                Divider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun GameCell(
    cell: Cell,
    location: Location,
    selectedLocation: Location,
    select: (Location, Int) -> Unit,
    modifier: Modifier
) {
    val boxModifier = if (cell.type == Cell.Type.BLANK) {
        Modifier
            .then(modifier)
            .background(
                color = if (location == selectedLocation) {
                    MaterialTheme.colorScheme.surfaceVariant
                } else {
                    MaterialTheme.colorScheme.surface
                }
            )
            .clickable { select(location, cell.num) }
    } else {
        Modifier
            .then(modifier)
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f))
    }
    val textColor = if (cell.type == Cell.Type.BLANK) {
        if (location == selectedLocation) {
            MaterialTheme.colorScheme.onSurfaceVariant
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }
    val numString = if (cell.num != 0) {
        "${cell.num}"
    } else {
        ""
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = boxModifier.fillMaxSize()
    ) {
        Text(
            text = numString,
            color = textColor
        )
    }

}

@Preview(
    name = "GameBoard",
    showBackground = true
)
@Preview(
    name = "GameBoard", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun GameBoardPreview() {
//    val boardInputStream = LocalContext.current.assets
//        .open("puzzle.json")
//    val boardJsonString = boardInputStream.bufferedReader().readText()
//    val board = Json.decodeFromString<List<List<Cell>>>(boardJsonString)
//    val puzzle = Puzzle(board = board)
//    AzureTheme {
//        GameBoard(
//            puzzle = puzzle,
//            selectLocation = {}
//        )
//    }
}
