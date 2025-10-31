package org.example.redcrosswalletapp.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.redcrosswalletapp.AppState
import org.example.redcrosswalletapp.ChallengeState
import org.example.redcrosswalletapp.ProgressState
import org.jetbrains.compose.resources.painterResource
import redcrosswalletapp.composeapp.generated.resources.Res
import redcrosswalletapp.composeapp.generated.resources.sprout
import redcrosswalletapp.composeapp.generated.resources.sprout_1
import redcrosswalletapp.composeapp.generated.resources.sprout_2
import redcrosswalletapp.composeapp.generated.resources.tree

/**
 * Progress screen displaying a progress bar, level info, and a donation UI.
 *
 * @param appState   Global state holder (contains ProgressState & ChallengeState).
 * @param onNavigateBack            Callback for the “Back” button.
 * @param onNavigateToChallenges    Callback for the “View Challenges” button.
 * @param modifier Optional root‑modifier.
 */
@Composable
fun ProgressScreen(
    appState: AppState,
    onNavigateBack: () -> Unit,
    onNavigateToChallenges: () -> Unit,
    modifier: Modifier = Modifier
) {
    // -----------------------------------------------------------------
    // Pull the pieces we need from AppState
    // -----------------------------------------------------------------
    val progressState = appState.progressState
    val totalPoints by appState.challengeState.totalPoints.collectAsState()

    // -----------------------------------------------------------------
    // Snackbar host – a single place where we’ll show transient messages
    // -----------------------------------------------------------------
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Helper that any part of this composable can call
    fun showMessage(message: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    // -----------------------------------------------------------------
    //    Layout – the whole screen lives inside a Box, so we can overlay
    //    the SnackbarHost at the bottom.
    // -----------------------------------------------------------------
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3FFC5)), // light‑green background
        contentAlignment = Alignment.Center
    ) {
        // Main content
        ProgressScreenContent(
            progressState = progressState,
            totalPoints = totalPoints,
            appState = appState,
            onNavigateBack = onNavigateBack,
            onNavigateToChallenges = onNavigateToChallenges,
            showMessage = ::showMessage          // pass the helper down
        )

        // Snackbar overlay (automatically appears at the bottom)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

/* -------------------------------------------------------------
   Inner UI
   ------------------------------------------------------------- */
@Composable
private fun ProgressScreenContent(
    progressState: ProgressState,
    totalPoints: Int,
    appState: AppState,
    onNavigateBack: () -> Unit,
    onNavigateToChallenges: () -> Unit,
    // Lambda that shows a transient message (snackbar)
    showMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // -----------------------------------------------------------------
    // Observe progress & level
    // -----------------------------------------------------------------
    val progress by progressState.progress.collectAsState()
    val level by progressState.level.collectAsState()

    // -----------------------------------------------------------------
    // Local UI state for the donation input
    // -----------------------------------------------------------------
    var countText by remember { mutableStateOf("") }

    // -----------------------------------------------------------------
    // Animated progress bar
    // -----------------------------------------------------------------
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "progress_animation"
    )

    // -----------------------------------------------------------------
    // Scroll handling (keyboard may push UI up)
    // -----------------------------------------------------------------
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // -------------------------------------------------------------
        // Plant graphic (sprout / tree)
        // -------------------------------------------------------------
        SproutTree(level = level)

        // -------------------------------------------------------------
        // Level label
        // -------------------------------------------------------------
        LevelDisplay(level = level)

        // -------------------------------------------------------------
        // Progress bar + numeric label
        // -------------------------------------------------------------
        ProgressIndicatorSection(
            animatedProgress = animatedProgress,
            totalPoints = totalPoints
        )

        // -------------------------------------------------------------
        // Control buttons (reset, navigation)
        // -------------------------------------------------------------
        ProgressControlButtons(onReset = { progressState.reset() })
        ChallengeButton(onClick = onNavigateToChallenges)
        NavigationButton(onNavigateBack = onNavigateBack)

        // -------------------------------------------------------------
        // *** Donation UI ***
        // -------------------------------------------------------------
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Text(
            text = "Donate clothing",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = countText,
            onValueChange = { newText ->
                // Accept only digits
                if (newText.all { it.isDigit() }) countText = newText
            },
            label = { Text("Number of clothing items") },
            placeholder = { Text("e.g. 5") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val count = countText.toIntOrNull()
                if (count == null || count <= 0) {
                    // Show a "snackbar" message
                    showMessage("Enter a positive number.")
                    return@Button
                }

                // Grant the points (10 pts per item)
                appState.donateClothing(count)

                // Confirmation snackbar
                showMessage(
                    "✅ Donated $count item(s) → +${count * ChallengeState.POINTS_PER_ITEM} points!"
                )

                // Reset the field for the next donation
                countText = ""
            },
            enabled = countText.isNotBlank(),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Donate (+${ChallengeState.POINTS_PER_ITEM} pts each)")
        }
    }
}

/* -------------------------------------------------------------
   Helper composables
   ------------------------------------------------------------- */
@Composable
private fun LevelDisplay(level: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Level $level",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ProgressIndicatorSection(
    animatedProgress: Float,
    totalPoints: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Donation Progress",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Total Points: $totalPoints",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        // LinearProgressIndicator expects a Float, not a lambda
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "${(animatedProgress * 100).toInt()}%",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ProgressControlButtons(onReset: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onReset,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset")
        }
    }
}

@Composable
private fun NavigationButton(onNavigateBack: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onNavigateBack,
        modifier = modifier.fillMaxWidth()
    ) {
        Text("Back to Home")
    }
}

@Composable
private fun ChallengeButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.width(200.dp).height(48.dp)
    ) {
        Text(text = "View Challenges")
    }
}

@Composable
private fun SproutTree(modifier: Modifier = Modifier, level: Int) {
    val imageResource = when (level) {
        1 -> Res.drawable.sprout
        2 -> Res.drawable.sprout_1
        3 -> Res.drawable.sprout_2
        else -> Res.drawable.tree
    }

    Image(
        painter = painterResource(imageResource),
        contentDescription = "Plant growth stage: Level $level",
        modifier = modifier.size(120.dp)
    )
}