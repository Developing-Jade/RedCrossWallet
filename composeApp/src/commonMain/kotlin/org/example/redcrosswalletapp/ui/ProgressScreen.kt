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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.redcrosswalletapp.ProgressState
import org.jetbrains.compose.resources.painterResource
import redcrosswalletapp.composeapp.generated.resources.Res
import redcrosswalletapp.composeapp.generated.resources.sprout
import redcrosswalletapp.composeapp.generated.resources.sprout_1
import redcrosswalletapp.composeapp.generated.resources.sprout_2
import redcrosswalletapp.composeapp.generated.resources.tree


/**
 * Progress screen displaying a progress bar with controls
 *
 * @param state The progress state to observe and control
 * @param onNavigateBack Callback invoked when user wants to navigate back
 * @param modifier Optional modifier for the root container
 */
@Composable
fun ProgressScreen(
    state: ProgressState,
    challengePoints: Int = 0,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3FFC5)), //Light green background color
        contentAlignment = Alignment.Center
    ) {
        ProgressScreenContent(
            state = state,
            challengePoints = challengePoints,
            onNavigateBack = onNavigateBack
        )
    }
}

@Composable
private fun ProgressScreenContent(
    state: ProgressState,
    challengePoints: Int,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    val progress by state.progress.collectAsState()

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "progress_animation"
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SproutTree(progress = animatedProgress)

        ProgressIndicatorSection(
            animatedProgress = animatedProgress,
            challengePoints = challengePoints
            )

        ProgressControlButtons(
            onAdvanceQuarter = { state.advanceQuarter() },
            onReset = { state.reset() }
        )

        NavigationButton(onNavigateBack = onNavigateBack)
    }
}

@Composable
private fun ProgressIndicatorSection(
    animatedProgress: Float,
    challengePoints: Int,
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
            text = "Total Points: $challengePoints",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "${(animatedProgress * 100).toInt()}%",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ProgressControlButtons(
    onAdvanceQuarter: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onAdvanceQuarter,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add 25%")
        }

        Button(
            onClick = onReset,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset")
        }
    }
}

@Composable
private fun NavigationButton(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onNavigateBack,
        modifier = modifier.fillMaxWidth()
    ) {
        Text("Back to Home")
    }
}

@Composable
private fun SproutTree(modifier: Modifier = Modifier, progress: Float) {
    val imageResource = when {
        progress < 0.25 -> Res.drawable.sprout
        progress < 0.5 -> Res.drawable.sprout_1
        progress < 0.75 -> Res.drawable.sprout_2
        else -> Res.drawable.tree
    }

    Image(
        painter = painterResource(imageResource),
        contentDescription = "Plant growth stage",
        modifier = modifier.size(120.dp)
    )
}