package com.zus.assesment.ui.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.zus.assesment.domain.model.CartLine
import com.zus.assesment.ui.components.SlantedEndShape
import com.zus.assesment.ui.components.SlantedShape
import com.zus.assesment.ui.theme.ZusBlack
import com.zus.assesment.ui.theme.ZusBlue
import com.zus.assesment.ui.theme.ZusGray
import com.zus.assesment.ui.theme.ZusWhite

private val CardShape = RoundedCornerShape(10.dp)
private val ImageWidth = 96.dp
private val CardMinHeight = 108.dp
private val ImageSlant = 12.dp
private val ButtonWidth = 38.dp
private val ButtonHeight = 32.dp
private val ButtonSlant = 7.dp

@Composable
fun CartItemCard(
    line: CartLine,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = CardMinHeight)
            .height(IntrinsicSize.Min)
            .clip(CardShape)
            .background(ZusWhite),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .width(ImageWidth)
                .fillMaxHeight()
                .clip(SlantedEndShape(slant = ImageSlant)),
        ) {
            AsyncImage(
                model = line.item.imageUrl,
                contentDescription = line.item.name,
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
                text = line.item.name.uppercase(),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontStyle = FontStyle.Normal,
                    lineHeight = 16.sp,
                ),
                color = ZusBlack,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "RM %.2f each".format(line.item.price),
                style = MaterialTheme.typography.bodySmall,
                color = ZusGray,
                modifier = Modifier.padding(top = 4.dp),
            )
            Text(
                text = "RM %.2f".format(line.lineTotal),
                style = MaterialTheme.typography.labelMedium,
                color = ZusBlue,
                modifier = Modifier.padding(top = 6.dp),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CartQuantityButton(label = "−", onClick = onDecrement)
                Box(
                    modifier = Modifier
                        .requiredWidth(24.dp)
                        .requiredHeight(ButtonHeight),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = line.quantity.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        color = ZusBlack,
                        textAlign = TextAlign.Center,
                    )
                }
                CartQuantityButton(label = "+", onClick = onIncrement)
            }
        }
    }
}

@Composable
private fun CartQuantityButton(
    label: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .requiredWidth(ButtonWidth)
            .requiredHeight(ButtonHeight)
            .defaultMinSize(minWidth = 0.dp, minHeight = 0.dp),
        shape = SlantedShape(slant = ButtonSlant),
        colors = ButtonDefaults.buttonColors(
            containerColor = ZusBlue,
            contentColor = ZusWhite,
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
