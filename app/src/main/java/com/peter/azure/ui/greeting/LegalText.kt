/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.greeting

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.azure.R
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
        color = MaterialTheme.colorScheme.secondary,
        fontWeight = FontWeight.Bold,
        textDecoration = TextDecoration.Underline
    )

    val annotatedText = buildAnnotatedString {
        append(stringResource(R.string.screen_greeting_slogan))

        if (isCompact) {
            appendLine()
        } else {
            append(" ")
        }

        append(stringResource(R.string.screen_greeting_legal_text_me))

        pushStringAnnotation(
            tag = infoTypeTag,
            annotation = Info.Type.SERVICE.name
        )
        withStyle(style = linkedStyle) {
            append(stringResource(R.string.service_title))
        }
        pop()

        append(stringResource(R.string.screen_greeting_legal_text_and))

        pushStringAnnotation(
            tag = infoTypeTag,
            annotation = Info.Type.PRIVACY.name
        )
        withStyle(style = linkedStyle) {
            append(stringResource(R.string.privacy_title))
        }
        pop()

        append(stringResource(R.string.screen_greeting_legal_text_period))
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
