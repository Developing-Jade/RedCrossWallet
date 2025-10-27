package org.example.redcrosswalletapp.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.redcrosswalletapp.ProgressState


class ProgressScreen {

    /**
     * Progress screen displaying a progress bar with controls
     *
     * @param state The progress state to observe and control
     * @param modifier Optional modifier for the root container
     */
    @Composable
    fun ProgressScreen(
        state: ProgressState,
        modifier: Modifier = Modifier
    ) {
        val progress by state.progress.collectAsState()

        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = tween(durationMillis = 300),
            label = "progress_animation"
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { state.advanceQuarter() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add 25%")
            }

            Button(
                onClick = { state.reset() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset")
            }
        }
    }
}