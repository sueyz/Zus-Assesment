package com.zus.assesment.ui.components

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class SlantedShape(
    private val slant: Dp = Dp(18f),
    private val invert: Boolean = false,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val slantPx = with(density) { slant.toPx() }
        val path = Path().apply {
            if (invert) {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width - slantPx, size.height)
                lineTo(slantPx, size.height)
            } else {
                moveTo(slantPx, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width - slantPx, size.height)
                lineTo(0f, size.height)
            }
            close()
        }
        return Outline.Generic(path)
    }
}

class SlantedEndShape(
    private val slant: Dp = 14.dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val slantPx = with(density) { slant.toPx() }
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width - slantPx, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}

class SloganBannerShape(
    private val slant: Dp = 14.dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val slantPx = with(density) { slant.toPx() }
        val path = Path().apply {
            moveTo(slantPx, 0f)
            lineTo(size.width - slantPx * 0.35f, 0f)
            lineTo(size.width, slantPx * 0.55f)
            lineTo(size.width - slantPx * 0.75f, size.height)
            lineTo(slantPx * 0.45f, size.height - slantPx * 0.35f)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}
