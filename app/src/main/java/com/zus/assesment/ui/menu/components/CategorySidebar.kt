package com.zus.assesment.ui.menu.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.zus.assesment.domain.model.MenuCategory
import com.zus.assesment.ui.components.SlantedShape
import com.zus.assesment.ui.theme.ZusBlue
import com.zus.assesment.ui.theme.ZusGray
import com.zus.assesment.ui.theme.ZusLightGray
import com.zus.assesment.ui.theme.ZusWhite

private val CardShape = RoundedCornerShape(
    topStart = 0.dp,
    topEnd = 10.dp,
    bottomEnd = 10.dp,
    bottomStart = 0.dp,
)
private val SidebarWidth = 136.dp

@Composable
fun CategorySidebar(
    categories: List<MenuCategory>,
    selectedCategoryId: String?,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .width(SidebarWidth)
                .fillMaxHeight()
                .padding(vertical = 7.dp),
    ) {
        CategorySectionTitle()

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(top = 10.dp)
                    .clip(CardShape)
                    .background(ZusWhite)
                    .verticalScroll(rememberScrollState()),
        ) {
            categories.forEachIndexed { index, category ->
                val selected = category.id == selectedCategoryId
                CategoryItem(
                    category = category,
                    selected = selected,
                    onClick = { onCategorySelected(category.id) },
                )
                val next = categories.getOrNull(index + 1)
                if (!selected && next != null && next.id != selectedCategoryId) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        color = ZusLightGray,
                        thickness = 1.dp,
                    )
                }
            }
        }
    }
}

@Composable
private fun CategorySectionTitle() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(
                modifier =
                    Modifier
                        .width(4.dp)
                        .height(16.dp)
                        .clip(SlantedShape(slant = 3.dp))
                        .background(ZusBlue),
            )
            Text(
                text = "CATEGORIES",
                style =
                    TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 9.sp,
                        letterSpacing = 1.2.sp,
                    ),
                color = ZusBlue,
                modifier = Modifier.padding(start = 6.dp),
            )
        }
        Spacer(
            modifier =
                Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(0.55f)
                    .height(2.dp)
                    .clip(SlantedShape(slant = 4.dp))
                    .background(ZusGray.copy(alpha = 0.35f)),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryItem(
    category: MenuCategory,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val fullDescription = category.description.trim()
    if (fullDescription.isBlank()) {
        CategoryItemContent(
            category = category,
            selected = selected,
            onClick = onClick,
        )
        return
    }

    val tooltipState = rememberTooltipState(isPersistent = true)

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Right),
        tooltip = {
            PlainTooltip {
                Text(
                    text = fullDescription,
                    style = TextStyle(fontSize = 12.sp, lineHeight = 16.sp),
                )
            }
        },
        state = tooltipState,
        enableUserInput = true,
    ) {
        CategoryItemContent(
            category = category,
            selected = selected,
            onClick = onClick,
        )
    }
}

@Composable
private fun CategoryItemContent(
    category: MenuCategory,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = if (selected) ZusWhite else ZusBlue

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(if (selected) ZusBlue else ZusWhite)
                .clickable(onClick = onClick)
                .padding(horizontal = 8.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = category.thumbnailUrl,
            contentDescription = category.name,
            modifier = Modifier.size(34.dp),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(contentColor),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category.name.uppercase(),
                style =
                    TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp,
                        letterSpacing = 0.3.sp,
                    ),
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = category.subtitle,
                style =
                    TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp,
                        lineHeight = 12.sp,
                    ),
                color = contentColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}

private val MenuCategory.subtitle: String
    get() {
        val sentence = description.substringBefore('.').trim()
        return if (sentence.length <= 36) sentence else sentence.take(33) + "..."
    }
