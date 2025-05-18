package com.phyohtet.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.phyohtet.game.model.Cell
import com.phyohtet.game.ui.Game2048
import com.phyohtet.game.ui.GameScreen
import com.phyohtet.game.ui.GameState
import com.phyohtet.game.ui.theme.MyTheme

@Composable
fun App(game: Game2048 = Game2048(4)) {
    var state by remember { mutableStateOf(GameState()) }

    LaunchedEffect(Unit) {
        game.initialize()
        state = GameState(
            cells = game.cells,
            score = game.score,
            bestScore = game.bestScore,
        )
    }

    MyTheme {
        GameScreen(
            state = state,
            onSwipe = { direction ->
                if (game.move(direction)) {
                    state = GameState(
                        cells = game.cells,
                        score = game.score,
                        bestScore = game.bestScore,
                    )
                }
            },
            restart = {
                game.initialize()
                state = GameState(
                    cells = game.cells,
                    score = game.score,
                    bestScore = game.bestScore,
                )
            }
        )
    }

}

@Composable
fun PreviewApp() {
    MyTheme {
        GameScreen(
            state = GameState(
                cells = listOf(Cell(0, 1).apply { value = 2 }),
            ),
            onSwipe = { },
            restart = { }
        )
    }
}