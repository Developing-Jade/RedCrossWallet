package org.example.redcrosswalletapp.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
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

// ──────────────────────────────────────────────────────────────
// Cosmetic drawables
// ──────────────────────────────────────────────────────────────
import redcrosswalletapp.composeapp.generated.resources.tree_valentines
import redcrosswalletapp.composeapp.generated.resources.tree_flowers
import redcrosswalletapp.composeapp.generated.resources.tree_easter
import redcrosswalletapp.composeapp.generated.resources.tree_apples
import redcrosswalletapp.composeapp.generated.resources.tree_halloween
import redcrosswalletapp.composeapp.generated.resources.tree_christmas

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
    // totalPoints is a Long – collect it as a State
    val totalPoints by appState.challengeState.totalPoints.collectAsState()
    // Visibility flag – already a State<Boolean> in AppState
    val isOnProgressScreen by appState.isOnProgressScreen

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
    // Layout – the whole screen lives inside a Box, so we can overlay
    // the SnackbarHost at the bottom.
    // -----------------------------------------------------------------
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // Main content
        ProgressScreenContent(
            progressState = progressState,
            totalPoints = totalPoints,
            appState = appState,
            isVisible = isOnProgressScreen,
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
    totalPoints: Long,
    appState: AppState,
    isVisible: Boolean,
    onNavigateBack: () -> Unit,
    onNavigateToChallenges: () -> Unit,
    // Lambda that shows a transient message (snackbar)
    showMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // -----------------------------------------------------------------
    // Observe progress, level, max‑level flag, and cosmetic index
    // -----------------------------------------------------------------
    val progress by progressState.progress.collectAsState()
    val level by progressState.level.collectAsState()
    val isMaxLevel by progressState.isMaxLevelFlow.collectAsState()
    val cosmeticIndex by progressState.cosmeticImageIndexFlow.collectAsState()

    // -----------------------------------------------------------------
    // Local UI state for the donation input
    // -----------------------------------------------------------------
    var countText by remember { mutableStateOf("") }

    // -----------------------------------------------------------------
    // Animated progress bar – only animate when the screen is visible
    // -----------------------------------------------------------------
    val targetProgress = if (isVisible) progress else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 300),
        label = "progress_animation"
    )

    // -----------------------------------------------------------------
    // Image fade‑in animation
    // -----------------------------------------------------------------
    val imageAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "image_fade"
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
        // Plant graphic **or** cosmetic carousel (same spot)
        // -------------------------------------------------------------
        val showCarousel = isMaxLevel && animatedProgress >= 1f

        if (showCarousel) {
            // ------- Cosmetic carousel replaces the tree image -------
            // 1️⃣ Define the list of drawables
            val cosmeticDrawables = listOf(
                Res.drawable.tree_valentines,
                Res.drawable.tree_flowers,
                Res.drawable.tree_easter,
                Res.drawable.tree_apples,
                Res.drawable.tree_halloween,
                Res.drawable.tree_christmas
            )

            // 2️⃣ Show the current cosmetic image with the same size/animation
            if (cosmeticDrawables.isNotEmpty()) {
                Image(
                    painter = painterResource(
                        cosmeticDrawables[cosmeticIndex % cosmeticDrawables.size]
                    ),
                    contentDescription = "Cosmetic ${cosmeticIndex + 1}",
                    modifier = Modifier
                        .size(120.dp)                     // same dimensions as SproutTree
                        .graphicsLayer { this.alpha = imageAlpha }
                )
            }

            // 3️⃣ Button to cycle to the next cosmetic image
            Button(
                onClick = {
                    // Tell ProgressState to advance the index
                    progressState.advanceCosmeticImage(cosmeticDrawables.size)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Next cosmetic")
            }
        } else {
            // ------- Normal sprout/tree when not max level -------
            SproutTree(
                level = level,
                alpha = imageAlpha
            )
        }

        // -------------------------------------------------------------
        // Level label (unchanged)
        // -------------------------------------------------------------
        LevelDisplay(level = level)

        // -------------------------------------------------------------
        // Progress bar + numeric label (unchanged)
        // -------------------------------------------------------------
        ProgressIndicatorSection(
            animatedProgress = animatedProgress,
            totalPoints = totalPoints
        )

        // -------------------------------------------------------------
        // Navigation buttons (unchanged)
        // -------------------------------------------------------------
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


        Button(
            onClick = {
                val count = countText.toIntOrNull()
                if (count == null || count <= 0) {
                    // Show a snackbar message
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
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Donate (+${ChallengeState.POINTS_PER_ITEM} pts each)")
        }

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
    totalPoints: Long,
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

        // LinearProgressIndicator is deprecated - should be replaced with a non-deprecated variant
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
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = "View Challenges")
    }
}

@Composable
private fun SproutTree(
    modifier: Modifier = Modifier,
    level: Int,
    alpha: Float = 1f
) {
    val imageResource = when (level) {
        1 -> Res.drawable.sprout
        2 -> Res.drawable.sprout_1
        3 -> Res.drawable.sprout_2
        else -> Res.drawable.tree
    }

    Image(
        painter = painterResource(imageResource),
        contentDescription = "Plant growth stage: Level $level",
        modifier = modifier
            .size(120.dp)
            .graphicsLayer { this.alpha = alpha }
    )
}