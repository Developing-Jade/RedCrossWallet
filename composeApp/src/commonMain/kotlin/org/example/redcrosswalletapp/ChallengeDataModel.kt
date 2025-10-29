package org.example.redcrosswalletapp

/**
 * Represents a sustainability challenge
 */
data class Challenge(
    val id: Int,
    val title: String,
    val description: String,
    val points: Int,
    val category: ChallengeCategory,
    val isCompleted: Boolean = false
)

/**
 * Categories for organizing challenges
 */
enum class ChallengeCategory(val displayName: String) {
    ENERGY("Energy"),
    WATER("Water"),
    WASTE("Waste"),
    TRANSPORT("Transport"),
    FOOD("Food")
}
