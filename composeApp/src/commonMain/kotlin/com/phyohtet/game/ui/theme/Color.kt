package com.phyohtet.game.ui.theme

import androidx.compose.ui.graphics.Color

val BackgroundColor = Color(0xfffaf8f0)

val BoardColor = Color(0xffbbada0)

val GridColor = Color(0xffcdc1b4)

val ButtonColor = Color(0xff661f1f)

val TitleColor = Color(0xff776e65)

data class CellColor(
    val background: Color,
    val color: Color,
)

val cellColors = mapOf(
    0 to CellColor(Color.Transparent, Color.Transparent),
    2 to CellColor(Color(0xffeee4da), Color(0xff776e65)),
    4 to CellColor(Color(0xffede0c8), Color(0xff776e65)),
    8 to CellColor(Color(0xfff2b179), Color(0xfff9f6f2)),
    16 to CellColor(Color(0xfff59563), Color(0xfff9f6f2)),
    32 to CellColor(Color(0xfff67c5f), Color(0xfff9f6f2)),
    64 to CellColor(Color(0xfff65e3b), Color(0xfff9f6f2)),
    128 to CellColor(Color(0xffedcf72), Color(0xfff9f6f2)),
    256 to CellColor(Color(0xffedcc61), Color(0xfff9f6f2)),
    512 to CellColor(Color(0xffedc850), Color(0xfff9f6f2)),
    1024 to CellColor(Color(0xffedc53f), Color(0xfff9f6f2)),
    2048 to CellColor(Color(0xffedc22e), Color(0xfff9f6f2)),
    4096 to CellColor(Color(0xff3c3a32), Color(0xfff9f6f2)),
)
