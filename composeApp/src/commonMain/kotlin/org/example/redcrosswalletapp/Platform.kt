package org.example.redcrosswalletapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform