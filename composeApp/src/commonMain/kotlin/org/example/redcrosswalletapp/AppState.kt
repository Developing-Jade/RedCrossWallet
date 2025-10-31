package org.example.redcrosswalletapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Application-level state holder
 * Manages current screen and shared state across screens
 */
class AppState {

    // A scope that lives as long as the AppState instance does
    private val uiScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // Screen navigation state
    private var _currentScreen by mutableStateOf(Screen.HOME)
        private set

    val currentScreen: Screen
        get() = _currentScreen

    // Shared features states
    val progressState = ProgressState()
    val challengeState = ChallengeState()


    // Level-up config
    private val pointsPerLevel = 50
    private val progressPerPoint: Float = 1f / pointsPerLevel

    // Remember the previous total points emitted by the flow
    private var previousTotalPoints: Long = 0L


    init {
        // Listen to the cumulative points flow
        challengeState.totalPoints
            .onEach { totalPoints ->

                //  Points earned since last advancement
                val delta = (totalPoints - previousTotalPoints).coerceAtLeast(0L)

                // Remember this value for the next round
                previousTotalPoints = totalPoints.toLong()

                // Add the appropriate fraction for each new point
                repeat(delta.toInt()) {
                    progressState.advance(progressPerPoint)
                }
            }
            .launchIn(uiScope)          // <-- use the persistent UI scope
    }

    /** True when the Progress screen is the current destination. */
    private val _isOnProgressScreen = mutableStateOf(false)
    val isOnProgressScreen: State<Boolean> = _isOnProgressScreen

    /** Navigate to a specific screen and update the visibility flag. */
    fun navigateTo(screen: Screen) {
        _currentScreen = screen
        // Update the flag – only true for PROGRESS, false otherwise
        _isOnProgressScreen.value = (screen == Screen.PROGRESS)
    }

    /** Shortcut for returning home (keeps the flag false). */
    fun navigateToHome() {
        navigateTo(Screen.HOME)
    }

    // UI-friendly call for donation
    fun donateClothing(itemCount: Int) {
        challengeState.donateClothing(itemCount)
    }
}