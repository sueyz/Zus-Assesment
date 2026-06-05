package com.zus.assesment.ui.menu.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zus.assesment.ui.components.SlantedShape
import com.zus.assesment.ui.menu.ItemFilter
import com.zus.assesment.ui.theme.ZusBlue
import com.zus.assesment.ui.theme.ZusWhite

private val FilterChipShape = SlantedShape(slant = 8.dp)

@Composable
fun FilterChips(
    selected: ItemFilter,
    onSelected: (ItemFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        ItemFilter.entries.forEach { filter ->
            val active = filter == selected
            FilterChip(
                selected = active,
                onClick = { onSelected(filter) },
                label = {
                    Text(
                        text = filter.name.replace('_', ' '),
                        style =
                            TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic,
                                fontSize = 12.sp,
                                letterSpacing = 0.3.sp,
                            ),
                    )
                },
                shape = FilterChipShape,
                colors =
                    FilterChipDefaults.filterChipColors(
                        containerColor = ZusWhite,
                        labelColor = ZusBlue,
                        selectedContainerColor = ZusBlue,
                        selectedLabelColor = ZusWhite,
                    ),
                border =
                    FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = active,
                        borderColor = ZusBlue,
                        selectedBorderColor = ZusBlue,
                        borderWidth = 1.dp,
                        selectedBorderWidth = 1.dp,
                    ),
                elevation =
                    FilterChipDefaults.filterChipElevation(
                        elevation = 0.dp,
                        pressedElevation = 0.dp,
                        focusedElevation = 0.dp,
                        hoveredElevation = 0.dp,
                        draggedElevation = 0.dp,
                        disabledElevation = 0.dp,
                    ),
            )
        }
    }
}
