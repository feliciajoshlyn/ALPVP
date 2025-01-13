package com.feliii.alpvp.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.feliii.alpvp.R
import com.feliii.alpvp.viewmodel.LeafFallViewModel
import com.feliii.alpvp.uiStates.LeafFallUiState
import com.feliii.alpvp.enums.PagesEnum
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun LeafFallMenuScreen(
    navController: NavController,
    leafFallViewModel: LeafFallViewModel
) {
    var selectedMode by remember { mutableStateOf<String>("") }
    var selectedMusic by remember { mutableStateOf<String>("") }

    val uiState: LeafFallUiState by leafFallViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "LeafFall",
            style = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Game Modes Selection
        Text("Select Game Mode", style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp))

        Button(
            onClick = { selectedMode = "Classic" },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Classic Mode")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { selectedMode = "Endless" },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Endless Mode")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { selectedMode = "Challenge" },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Challenge Mode")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Top Score Display
        Text("Top Score: ${uiState.highScore}", style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp))

        Spacer(modifier = Modifier.height(20.dp))

        // Music selection
        Text("Select Music", style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp))

        Column(
            modifier = Modifier.selectableGroup()
        ) {
            val musicList = listOf("Music 1", "Music 2", "Music 3", "Music 4", "Music 5", "Music 6")

            musicList.forEach { music ->
                Button(
                    onClick = { selectedMusic = music },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(music)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Display selected music
        Text("Selected Music: $selectedMusic", style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp))

        Spacer(modifier = Modifier.height(20.dp))

        // Start Button
        Button(
            onClick = {
                if (selectedMode.isNotEmpty() && selectedMusic.isNotEmpty()) {
                    leafFallViewModel.startGame(1.toString(), selectedMode) // Example with userId = 1
                    navController.navigate(PagesEnum.LeafFallGame.name)
                } else {
                    Toast.makeText(
                        navController.context,
                        "Please select mode and music",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Game")
        }
    }
}
