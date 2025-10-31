package org.example.redcrosswalletapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.example.redcrosswalletapp.ui.ChallengeScreen
import org.example.redcrosswalletapp.ui.HomeScreen
import org.example.redcrosswalletapp.ui.ProgressScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Represents the different screens/destinations in the app
 */
enum class Screen {
    HOME,
    PROGRESS,
    CHALLENGE
}

/**
 * Main application composable.
 * Manages navigation between different screens.
 */
@Composable
@Preview
fun App() {
    // One AppState for the whole lifetime of the composable tree
    val appState = remember { AppState() }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AppContent(appState = appState)
        }
    }
}

/**
 * Switches between the three screens based on `appState.currentScreen`.
 */
@Composable
private fun AppContent(appState: AppState) {
    // Observe total points only for UI purposes
    val challengePoints by appState.challengeState.totalPoints.collectAsState()

    when (appState.currentScreen) {
        Screen.HOME -> {
            HomeScreen(
                onNavigateToProgress = { appState.navigateTo(Screen.PROGRESS) }
            )
        }

        Screen.PROGRESS -> {
            // ProgressScreen takes the whole AppState
            ProgressScreen(
                appState = appState,
                onNavigateToChallenges = { appState.navigateTo(Screen.CHALLENGE) },
                onNavigateBack = { appState.navigateToHome() }
            )
        }

        Screen.CHALLENGE -> {
            ChallengeScreen(
                state = appState.challengeState,
                // The challenge screen already knows how to navigate back
                onNavigateBack = { appState.navigateToHome() }
            )
        }
    }
}