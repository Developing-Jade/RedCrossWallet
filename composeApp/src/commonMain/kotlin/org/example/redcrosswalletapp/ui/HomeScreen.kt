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
import redcrosswalletapp.composeapp.generated.resources.red_cross_seed

/**
 * Home screen displaying welcome message and call-to-action button
 *
 * @param title Main title text
 * @param subtitle Secondary text below title
 * @param onStartClicked Callback invoked when the donation button is clicked
 * @param modifier Optional modifier for the root container
 */
@Composable
fun HomeScreen(
    title: String = "Welcome",
    subtitle: String = "Red Cross wallet",
    onStartClicked: () -> Unit,
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
            onStartClicked = onStartClicked
        )
    }
}

@Composable
private fun HomeScreenContent(
    title: String,
    subtitle: String,
    onStartClicked: () -> Unit
) {
    RedCrossLogo()
    
    Spacer(modifier = Modifier.height(24.dp))
    
    WelcomeText(title = title, subtitle = subtitle)
    
    Spacer(modifier = Modifier.height(48.dp))
    
    DonationButton(onClick = onStartClicked)
}

@Composable
private fun RedCrossLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(Res.drawable.red_cross_seed),
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
        Text(text = "Donation")
    }
}