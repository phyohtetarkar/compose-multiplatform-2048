package com.phyohtet.game

import androidx.compose.ui.window.ComposeUIViewController
import com.phyohtet.game.ui.Game2048
import platform.Foundation.NSUserDefaults

fun MainViewController() = ComposeUIViewController {
    val storage = IOSScoreStorage(NSUserDefaults.standardUserDefaults)
    val game = Game2048(4, storage)
    App(game)
}