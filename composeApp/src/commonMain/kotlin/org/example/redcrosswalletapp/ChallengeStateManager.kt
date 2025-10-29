package org.example.redcrosswalletapp

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages the state for challenges and points
 */
class ChallengeState {
    private val _challenges = mutableStateListOf<Challenge>().apply {
        addAll(getInitialChallenges())
    }
    val challenges: SnapshotStateList<Challenge> = _challenges

    private val _totalPoints = MutableStateFlow(0)
    val totalPoints: StateFlow<Int> = _totalPoints.asStateFlow()

    /**
     * Mark a challenge as completed and award points
     */
    fun completeChallenge(challengeId: Int) {
        val index = _challenges.indexOfFirst { it.id == challengeId }
        if (index != -1 && !_challenges[index].isCompleted) {
            val challenge = _challenges[index]
            _challenges[index] = challenge.copy(isCompleted = true)
            _totalPoints.value += challenge.points
        }
    }

    /**
     * Reset a challenge
     */
    fun resetChallenge(challengeId: Int) {
        val index = _challenges.indexOfFirst { it.id == challengeId }
        if (index != -1 && _challenges[index].isCompleted) {
            val challenge = _challenges[index]
            _challenges[index] = challenge.copy(isCompleted = false)
            _totalPoints.value -= challenge.points
        }
    }

    private fun getInitialChallenges() = listOf(
        Challenge(
            id = 1,
            title = "Use Reusable Water Bottle",
            description = "Use a reusable water bottle for a whole day instead of single-use plastic",
            points = 10,
            category = ChallengeCategory.WASTE
        ),
        Challenge(
            id = 2,
            title = "Turn Off Lights",
            description = "Turn off all lights when leaving a room for one day",
            points = 15,
            category = ChallengeCategory.ENERGY
        ),
        Challenge(
            id = 3,
            title = "Take Shorter Showers",
            description = "Reduce shower time by 5 minutes",
            points = 20,
            category = ChallengeCategory.WATER
        ),
        Challenge(
            id = 4,
            title = "Bike or Walk",
            description = "Use bike or walk instead of driving for short trips",
            points = 25,
            category = ChallengeCategory.TRANSPORT
        ),
        Challenge(
            id = 5,
            title = "Meatless Monday",
            description = "Go vegetarian for one day",
            points = 30,
            category = ChallengeCategory.FOOD
        ),
        Challenge(
            id = 6,
            title = "Unplug Devices",
            description = "Unplug electronic devices when not in use",
            points = 15,
            category = ChallengeCategory.ENERGY
        ),
        Challenge(
            id = 7,
            title = "Recycle Properly",
            description = "Sort and recycle all waste for one day",
            points = 20,
            category = ChallengeCategory.WASTE
        ),
        Challenge(
            id = 8,
            title = "Reusable Shopping Bags",
            description = "Use reusable bags for all shopping trips",
            points = 10,
            category = ChallengeCategory.WASTE
        ),
        Challenge(
            id = 9,
            title = "Public Transport",
            description = "Use public transportation for your commute",
            points = 25,
            category = ChallengeCategory.TRANSPORT
        ),
        Challenge(
            id = 10,
            title = "Reduce Food Waste",
            description = "Plan meals and use all leftovers",
            points = 20,
            category = ChallengeCategory.FOOD
        )
    )
}
