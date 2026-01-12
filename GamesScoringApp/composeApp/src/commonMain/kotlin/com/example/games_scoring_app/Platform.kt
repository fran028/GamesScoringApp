package com.example.games_scoring_app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform