package com.feliii.alpvp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun WhackAMoleGame() {
    val gridSize = 3
    var score by remember { mutableStateOf(0) }
    val activeMole by produceState<Int>(-1) {
        while (true) {
            delay(1000L) // Moles appear every second
            value = (0 until gridSize * gridSize).random()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Score: $score")

        LazyVerticalGrid(
            columns = GridCells.Fixed(gridSize),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(gridSize * gridSize) { index ->
                val isMole = index == activeMole
                Button(
                    onClick = {
                        if (isMole) {
                            score++
                            // Reset activeMole to -1 after a successful whack
                            // You might need to adjust this based on your game logic
                            // activeMole = -1
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMole) Color.Green else Color.Gray
                    ),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxSize()
                ) {}
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WhackAMoleViewPreview() {
    WhackAMoleGame()
}