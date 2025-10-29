package org.example.redcrosswalletapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
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
 * Main application composable
 * Manages navigation between different screens
 */
@Composable
@Preview
fun App() {
    val appState = remember { AppState() }
    
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AppContent(appState = appState)
        }
    }
}

@Composable
private fun AppContent(appState: AppState) {
    when (appState.currentScreen) {
        Screen.HOME -> {
            HomeScreen(
                onNavigateToProgress = {
                    appState.navigateTo(Screen.PROGRESS)
                },
                onNavigateToChallenges = {
                    appState.navigateTo(Screen.CHALLENGE)
                }
            )
        }
        Screen.PROGRESS -> {
            ProgressScreen(
                state = appState.progressState,
                onNavigateBack = { appState.navigateToHome() }
            )
        }
        Screen.CHALLENGE -> {
            ChallengeScreen(
                state = appState.challengeState,
                onNavigateBack = { appState.navigateToHome() }
            )
        }
    }
}


