package com.phyohtet.game.model

import com.phyohtet.game.randomUUID

data class Cell(
    var x: Int,
    var y: Int,
    var value: Int = 0,
) {
    val id: String = randomUUID()
}