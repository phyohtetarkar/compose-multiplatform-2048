package com.phyohtet.game

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun randomUUID(): String