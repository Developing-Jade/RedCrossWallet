package org.example.redcrosswalletapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import redcrosswalletapp.composeapp.generated.resources.Res
import redcrosswalletapp.composeapp.generated.resources.app_logo

/**
 * Home screen displaying welcome message and call-to-action button
 *
 * @param onNavigateToProgress Callback invoked when user wants to navigate to progress screen
 * @param title Main title text
 * @param subtitle Secondary text below title
 * @param modifier Optional modifier for the root container
 */
@Composable
fun HomeScreen(
    onNavigateToProgress: () -> Unit,
    title: String = "Welcome",
    subtitle: String = "Red Cross emissions wallet",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HomeScreenContent(
            title = title,
            subtitle = subtitle,
            onNavigateToProgress = onNavigateToProgress
        )
    }
}

@Composable
private fun HomeScreenContent(
    title: String,
    subtitle: String,
    onNavigateToProgress: () -> Unit
) {
    RedCrossLogo()

    Spacer(modifier = Modifier.height(24.dp))

    WelcomeText(title = title, subtitle = subtitle)

    Spacer(modifier = Modifier.height(48.dp))

    DonationButton(onClick = onNavigateToProgress)
}

@Composable
private fun RedCrossLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(Res.drawable.app_logo),
        contentDescription = "Red Cross Logo",
        modifier = modifier.size(120.dp)
    )
}

@Composable
private fun WelcomeText(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun DonationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .width(200.dp)
            .height(48.dp)
    ) {
        Text(text = "Start Donation")
    }
}