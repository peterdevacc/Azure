/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.greeting

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.azure.data.entity.Info

@Composable
fun LegalText(
    isCompact: Boolean,
    loadInfo: (Info.Type) -> Unit
) {
    val infoTypeTag = "infoType"
    val fontSize = if (isCompact) {
        16
    } else {
        18
    }
    val linkedStyle = SpanStyle(
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )

    val annotatedText = buildAnnotatedString {
        append("Please read our ")

        pushStringAnnotation(
            tag = infoTypeTag,
            annotation = Info.Type.SERVICE.name
        )
        withStyle(style = linkedStyle) {
            append("service terms")
        }
        pop()

        append(", ")

        pushStringAnnotation(
            tag = infoTypeTag,
            annotation = Info.Type.SERVICE.name
        )
        withStyle(style = linkedStyle) {
            append("private policy")
        }
        pop()

        append(" and ")

        pushStringAnnotation(
            tag = infoTypeTag,
            annotation = Info.Type.SERVICE.name
        )
        withStyle(style = linkedStyle) {
            append("acknowledgements")
        }
        pop()

        append(".")
    }

    ClickableText(
        text = annotatedText,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = fontSize.sp,
        ),
        onClick = { offset ->
            annotatedText.getStringAnnotations(
                tag = infoTypeTag, start = offset, end = offset
            ).firstOrNull()?.let { annotation ->
                loadInfo(Info.Type.valueOf(annotation.item))
            }
        },
        modifier = Modifier.padding(bottom = 16.dp)
    )

}
