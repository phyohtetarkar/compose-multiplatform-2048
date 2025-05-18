package com.phyohtet.game

import platform.Foundation.NSUserDefaults

class IOSScoreStorage(private val defaults: NSUserDefaults): ScoreStorage {
    override fun save(score: Int) {
        defaults.setInteger(score.toLong(), ScoreStorage.KEY)
    }

    override fun getScore(): Int {
        return  defaults.integerForKey(ScoreStorage.KEY).toInt()
    }

}