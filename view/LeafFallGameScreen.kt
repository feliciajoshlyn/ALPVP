package com.feliii.alpvp.view

import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.feliii.alpvp.enums.PagesEnum
import com.feliii.alpvp.viewmodel.LeafFallViewModel
import com.feliii.alpvp.uiStates.LeafFallUiState
import kotlinx.coroutines.delay

@Composable
fun LeafFallGameScreen(
    navController: NavController,
    leafFallViewModel: LeafFallViewModel,
    gameMode: String // "Classic", "Endless", "Challenge"
) {
    // State variables
    var uiState by remember { mutableStateOf(LeafFallUiState()) }
    var score by remember { mutableStateOf(0) }
    var failedAttempts by remember { mutableStateOf(0) }
    var timer by remember { mutableStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    // Music setup
    val musicPlayer = remember { MediaPlayer.create(navController.context, R.raw.game_music) }

    // Start music if it's not playing already
    LaunchedEffect(Unit) {
        musicPlayer.start()
    }

    // Game timer and speed increment for Classic and Endless mode
    LaunchedEffect(gameMode) {
        if (gameMode == "Endless" || gameMode == "Classic") {
            while (!isGameOver) {
                delay(1000)
                timer++
                if (timer % 5 == 0) {
                    // Increase the speed of falling leaves every 5 seconds
                    leafFallViewModel.increaseFallSpeed()
                }
            }
        }
    }

    // Observe the uiState from the ViewModel
    val state by leafFallViewModel.uiState.collectAsState()

    // Update uiState when it changes
    uiState = state

    // Check if game is over
    if (failedAttempts >= 3 || score >= 100) {
        isGameOver = true
    }

    // Cleanup music player when game is over or user navigates
    DisposableEffect(Unit) {
        onDispose {
            musicPlayer.release()  // Stop and release the media player when done
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Mode: $gameMode", style = androidx.compose.ui.text.TextStyle(fontSize = 24.sp))

        // Score Display
        Text("Score: $score", style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp))
        Text("Failed Attempts: $failedAttempts", style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp))
        Text("Time: $timer sec", style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp))

        // Game Content - Leaf Animations and Falling Logic
        if (!isGameOver) {
            // Implement the falling leaf animation
            FallingLeavesAnimation(
                leafTypeList = uiState.fallingLeaves,
                onLeafClicked = { leaf ->
                    if (leaf != "bomb") {
                        score++
                    } else {
                        failedAttempts++
                    }
                }
            )
        } else {
            // Game Over Screen
            Text("Game Over! Top Score: ${uiState.highScore}")
            Button(onClick = { navController.navigate(PagesEnum.LeafFallMenu.name) }) {
                Text("Back to Menu")
            }
        }
    }
}
