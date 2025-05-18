package com.phyohtet.game

import android.content.SharedPreferences
import androidx.core.content.edit

class AndroidScoreStorage(private val sharePref: SharedPreferences) : ScoreStorage {

    override fun save(score: Int) {
        sharePref.edit(true) {
            putInt(ScoreStorage.KEY, score)
        }
    }

    override fun getScore(): Int {
        return sharePref.getInt(ScoreStorage.KEY, 0)
    }

}