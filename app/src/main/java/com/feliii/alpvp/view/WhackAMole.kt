package com.feliii.alpvp.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

@SuppressLint("ProduceStateDoesNotAssignValue")
@Composable
fun WhackAMoleGame(navController: NavController , gameMode: String) {
    var score by remember { mutableIntStateOf(0) }
    var activeMole by remember { mutableIntStateOf(-1) }
    var timeRemaining by remember { mutableIntStateOf(30) }

    //gridsize 4x4 for intense
    val gridSize = when (gameMode) {
        "intense" -> 4
        else -> 3
    }

    //waktu lebih cepat untuk intense
    val moleAppearanceDelay = when (gameMode) {
        "intense" -> 500L
        else -> 1000L
    }

    //check if it's timed(/intense)
    val isTimedMode = gameMode in listOf("timed", "intense") // Check if the mode is timed

    //Mole appearing
    LaunchedEffect (Unit) {
        while (true && timeRemaining != 0) {
            //mole appearing w delay
            delay(moleAppearanceDelay)
            //after delay mole muncul
            activeMole = (0 until gridSize * gridSize).random()
        }
    }

    //countdown (use int then delay 1000ms)
    LaunchedEffect(isTimedMode) {
        if (isTimedMode) {
            while (timeRemaining > 0) {
                delay(1000L)
                timeRemaining--
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display game information
        Text(text = "Mode: ${gameMode.capitalize()}")
        Text(text = "Score: $score")
        if (isTimedMode) {
            Text(text = "Time Remaining: $timeRemaining")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Game grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(gridSize),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(gridSize * gridSize) { index ->
                val isMole = index == activeMole
                Button(
                    onClick = {
                        if (isMole) {
                            score++
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMole) Color.Green else Color.Gray
                    ),
                    modifier = Modifier
                        .aspectRatio(1f)
                ) {}
            }
        }

        if (isTimedMode && timeRemaining == 0) {
            activeMole = -1 // Mole stop
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Game Over! Final Score: $score", color = Color.Red)
            //popbackstack is app screen rn is removed then muncul screen before
            Button(onClick = { navController.popBackStack() }) {
                Text(text = "Return to Menu")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WhackAMoleViewPreview() {
    WhackAMoleGame( navController = rememberNavController(), gameMode = "intense" )
}