package org.example.redcrosswalletapp.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.redcrosswalletapp.Challenge
import org.example.redcrosswalletapp.ChallengeCategory
import org.example.redcrosswalletapp.ChallengeState

/**
 * Challenge screen displaying sustainability challenges
 *
 * @param state The challenge state to observe and control
 * @param onNavigateBack Callback invoked when user wants to navigate back
 * @param modifier Optional modifier for the root container
 */
@Composable
fun ChallengeScreen(
    state: ChallengeState,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalPoints by state.totalPoints.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        ChallengeHeader(
            totalPoints = totalPoints,
            onNavigateBack = onNavigateBack
        )

        Spacer(modifier = Modifier.height(16.dp))

        ChallengeList(
            challenges = state.challenges,
            onChallengeToggle = { challengeId, isCompleted ->
                if (isCompleted) {
                    state.resetChallenge(challengeId)
                } else {
                    state.completeChallenge(challengeId)
                }
            }
        )
    }
}

@Composable
private fun ChallengeHeader(
    totalPoints: Long   ,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Sustainability Challenges",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Points Earned",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$totalPoints pts",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onNavigateBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go Back")
        }
    }
}

@Composable
private fun ChallengeList(
    challenges: List<Challenge>,
    onChallengeToggle: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(challenges) { challenge ->
            ChallengeCard(
                challenge = challenge,
                onToggle = { onChallengeToggle(challenge.id, challenge.isCompleted) }
            )
        }
    }
}

@Composable
private fun ChallengeCard(
    challenge: Challenge,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (challenge.isCompleted) {
            Color(0xFFE8F5E9) // Light green
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "background_color"
    )

    val scale by animateFloatAsState(
        targetValue = if (challenge.isCompleted) 0.98f else 1f,
        label = "scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (challenge.isCompleted) 2.dp else 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryBadge(category = challenge.category)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${challenge.points} pts",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = challenge.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = challenge.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = onToggle,
                modifier = Modifier.width(100.dp)
            ) {
                Text(
                    text = if (challenge.isCompleted) "Undo" else "Complete"
                )
            }
        }
    }
}

@Composable
private fun CategoryBadge(
    category: ChallengeCategory,
    modifier: Modifier = Modifier
) {
    val categoryColor = when (category) {
        ChallengeCategory.ENERGY -> Color(0xFFFFF9C4)
        ChallengeCategory.WATER -> Color(0xFFB3E5FC)
        ChallengeCategory.WASTE -> Color(0xFFC8E6C9)
        ChallengeCategory.TRANSPORT -> Color(0xFFFFCCBC)
        ChallengeCategory.FOOD -> Color(0xFFF8BBD0)
    }

    Text(
        text = category.displayName,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier
            .background(
                color = categoryColor,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        fontWeight = FontWeight.Medium
    )
}
