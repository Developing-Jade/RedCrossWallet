package org.example.redcrosswalletapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.example.redcrosswalletapp.ui.HomeScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import redcrosswalletapp.composeapp.generated.resources.Res
import redcrosswalletapp.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    //------------ APP-LEVEL STATE ----------------------------------
    var showingHome by remember { mutableStateOf(true) }

    //------------ THEME --------------------------------------------
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            if (showingHome) {
                HomeScreen (onStartClicked = { showingHome = false })
            }
        }
    }
}


