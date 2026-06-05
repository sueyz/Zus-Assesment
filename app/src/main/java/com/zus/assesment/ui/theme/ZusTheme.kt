package com.zus.assesment.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val ZusColorScheme =
    lightColorScheme(
        primary = ZusBlue,
        onPrimary = ZusWhite,
        secondary = ZusBlueDark,
        onSecondary = ZusWhite,
        background = ZusBackground,
        onBackground = ZusBlack,
        surface = ZusWhite,
        onSurface = ZusBlack,
        error = ZusBlueDark,
    )

@Composable
fun ZusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ZusColorScheme,
        typography =
            MaterialTheme.typography.copy(
                headlineMedium =
                    TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        letterSpacing = 1.sp,
                    ),
                titleMedium =
                    TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp,
                    ),
                bodyMedium =
                    TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                    ),
                labelMedium =
                    TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                    ),
                titleSmall =
                    TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp,
                        letterSpacing = 0.5.sp,
                    ),
                bodySmall =
                    TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp,
                        lineHeight = 13.sp,
                    ),
            ),
        content = content,
    )
}
