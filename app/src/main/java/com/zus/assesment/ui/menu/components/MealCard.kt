package com.zus.assesment.ui.menu.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.zus.assesment.domain.model.MenuItem
import com.zus.assesment.ui.components.SlantedEndShape
import com.zus.assesment.ui.components.SlantedShape
import com.zus.assesment.ui.theme.ZusBlack
import com.zus.assesment.ui.theme.ZusBlue
import com.zus.assesment.ui.theme.ZusGray
import com.zus.assesment.ui.theme.ZusLightGray
import com.zus.assesment.ui.theme.ZusWhite

private val MealCardShape = RoundedCornerShape(10.dp)
private val MealImageWidth = 108.dp
private val MealCardHeight = 116.dp
private val MealImageSlant = 14.dp
private val CartButtonWidth = 38.dp
private val CartButtonHeight = 32.dp
private val CartButtonSlant = 7.dp

@Composable
fun MealCard(
    item: MenuItem,
    quantity: Int,
    onAdd: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardAlpha = if (item.isAvailable) 1f else 0.55f

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .alpha(cardAlpha)
                .clip(MealCardShape)
                .background(ZusWhite),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = MealCardHeight)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .width(MealImageWidth)
                    .fillMaxHeight()
                    .clip(SlantedEndShape(slant = MealImageSlant)),
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
            ) {
                Text(
                    text = item.name.uppercase(),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontStyle = FontStyle.Normal,
                        lineHeight = 16.sp,
                    ),
                    color = ZusBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = ZusGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp),
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    item.tags.forEach { tag ->
                        Text(
                            text = tag.uppercase(),
                            style = MaterialTheme.typography.bodySmall,
                            color = ZusBlue,
                            modifier = Modifier
                                .background(ZusLightGray, RoundedCornerShape(2.dp))
                                .padding(horizontal = 5.dp, vertical = 1.dp),
                        )
                    }
                }
                if (item.isAvailable) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (quantity > 0) {
                            SlantedIconButton(
                                label = "−",
                                enabled = true,
                                onClick = onDecrement,
                            )
                            Box(
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(CartButtonHeight),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = quantity.toString(),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = ZusBlack,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                        SlantedIconButton(
                            label = "+",
                            enabled = true,
                            onClick = onAdd,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SlantedIconButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .width(CartButtonWidth)
            .height(CartButtonHeight)
            .defaultMinSize(minWidth = 0.dp, minHeight = 0.dp),
        shape = SlantedShape(slant = CartButtonSlant),
        colors = ButtonDefaults.buttonColors(
            containerColor = ZusBlue,
            contentColor = ZusWhite,
            disabledContainerColor = ZusLightGray,
            disabledContentColor = ZusGray,
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
            disabledElevation = 0.dp,
        ),
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}
