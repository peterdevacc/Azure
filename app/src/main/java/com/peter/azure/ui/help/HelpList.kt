/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.help

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.peter.azure.data.entity.Help

@Composable
fun HelpList(
    isPortrait: Boolean,
    helpMap: Map<Help.Catalog, List<Help>>
) {
    if (isPortrait) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            getHelpItems(true, helpMap)
        }
    } else {
        LazyRow(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxSize()
        ) {
            getHelpItems(false, helpMap)
        }
    }
}

private fun LazyListScope.getHelpItems(
    isPortrait: Boolean,
    helpMap: Map<Help.Catalog, List<Help>>
) = items(helpMap.toList()) { (catalog, helpList) ->
    val colorPair = getHelpItemColorPair(catalog)
    val itemModifier: Modifier
    val spacerModifier: Modifier

    if (isPortrait) {
        itemModifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(colorPair.first)
            .padding(16.dp)
        spacerModifier = Modifier.padding(vertical = 8.dp)
    } else {
        itemModifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(colorPair.first)
            .padding(16.dp)
            .width(320.dp)
            .fillMaxHeight()
        spacerModifier = Modifier.padding(horizontal = 8.dp)
    }

    Column(modifier = itemModifier) {
        Text(
            text = catalog.name,
            style = MaterialTheme.typography.titleLarge,
            color = colorPair.second.copy(alpha = 0.7f),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        )
        if (isPortrait) {
            helpList.forEach { help ->
                HelpItem(
                    help = help,
                    colorPair = colorPair,
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 4.dp)
                        .fillMaxWidth()
                )
            }
        } else {
            LazyColumn {
                items(helpList) { help ->
                    HelpItem(
                        help = help,
                        colorPair = colorPair,
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 4.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }

    Spacer(modifier = spacerModifier)
}

@Composable
private fun getHelpItemColorPair(catalog: Help.Catalog): Pair<Color, Color> {
    return when (catalog) {
        Help.Catalog.FAQ -> Pair(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            MaterialTheme.colorScheme.onPrimaryContainer
        )
        Help.Catalog.TUTORIAL -> Pair(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
