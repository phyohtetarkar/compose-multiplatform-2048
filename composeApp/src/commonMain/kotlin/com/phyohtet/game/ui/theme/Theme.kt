package com.phyohtet.game.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val colorScheme = lightColors(
    primary = ButtonColor,
    surface = BackgroundColor,
    background = BackgroundColor,
)

@Composable
fun MyTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = colorScheme,
        content = content,
    )
}