package com.phyohtet.game

interface ScoreStorage {
    companion object {
        const val KEY = "2048-best-score"
    }

    fun save(score: Int)

    fun getScore(): Int
}