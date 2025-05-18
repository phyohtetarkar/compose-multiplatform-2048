package com.phyohtet.game

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.phyohtet.game.ui.Game2048

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val storage = AndroidScoreStorage(getPreferences(Context.MODE_PRIVATE))
        val game = Game2048(4, storage)

        setContent {
            App(game)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AppAndroidPreview() {
    PreviewApp()
}