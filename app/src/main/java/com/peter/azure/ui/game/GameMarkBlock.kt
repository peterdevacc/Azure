package com.peter.azure.ui.game

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.peter.azure.R
import com.peter.azure.data.entity.Mark

@Composable
fun GameMarkBlock(
    markList: List<Mark>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f))
            .border(1.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        val times = 3
        for (i in 0..2) {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                for (j in 0..2) {
                    val num = (j + 1) + (i * times)
                    val mark = markList[num - 1]
                    ConstraintLayout(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    ) {
                        val (text, icon) = createRefs()
                        Text(
                            text = "$num",
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.constrainAs(text) {
                                centerVerticallyTo(parent)
                                centerHorizontallyTo(parent)
                            }
                        )
                        when (mark) {
                            Mark.Potential -> {
                                MarkIcon(
                                    R.drawable.ic_star_24,
                                    R.string.icon_cd_mark_potential,
                                    Modifier.constrainAs(icon) {
                                        end.linkTo(parent.end)
                                        bottom.linkTo(parent.bottom)
                                    }
                                )
                            }
                            Mark.WRONG -> {
                                MarkIcon(
                                    R.drawable.ic_cross_24,
                                    R.string.icon_cd_mark_wrong,
                                    Modifier.constrainAs(icon) {
                                        end.linkTo(parent.end)
                                        bottom.linkTo(parent.bottom)
                                    }
                                )
                            }
                            Mark.NONE -> {
                                Spacer(
                                    modifier = Modifier
                                        .padding(bottom = 1.dp)
                                        .size(16.dp)
                                        .constrainAs(icon) {
                                            end.linkTo(parent.end)
                                            bottom.linkTo(parent.bottom)
                                        }
                                )
                            }
                        }
                    }
                    if (j != 2) {
                        Divider(
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
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
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }

}

@Composable
private fun MarkIcon(
    @DrawableRes
    markIconId: Int,
    markIconContentDescriptionId: Int,
    modifier: Modifier
) {
    Icon(
        painter = painterResource(markIconId),
        contentDescription = stringResource(markIconContentDescriptionId),
        tint = MaterialTheme.colorScheme.onTertiaryContainer,
        modifier = modifier.then(
            Modifier
                .padding(end = 1.dp, bottom = 1.dp)
                .size(16.dp)
        )
    )
}
