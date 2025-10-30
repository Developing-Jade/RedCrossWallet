package org.example.redcrosswalletapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Application-level state holder
 * Manages current screen and shared state across screens
 */
class AppState {
    private var _currentScreen by mutableStateOf(Screen.HOME)
        private set

    val currentScreen: Screen
        get() = _currentScreen

    val progressState = ProgressState()
    val challengeState = ChallengeState()

    /**
     * Navigate to a specific screen
     */
    fun navigateTo(screen: Screen) {
        _currentScreen = screen
    }

    /**
     * Navigate back to home screen
     */
    fun navigateToHome() {
        navigateTo(Screen.HOME)
    }
}