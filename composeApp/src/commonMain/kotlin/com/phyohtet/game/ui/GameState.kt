package com.phyohtet.game.ui

import com.phyohtet.game.model.Cell

data class GameState(
    val cells: List<Cell> = listOf(),
    val score: Int = 0,
    val bestScore: Int = 0,
)