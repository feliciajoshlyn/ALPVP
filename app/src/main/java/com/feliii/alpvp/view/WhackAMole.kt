package com.feliii.alpvp.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.feliii.alpvp.enums.PagesEnum
import com.feliii.alpvp.viewmodel.HomeViewModel
import com.feliii.alpvp.viewmodel.WAMViewModel
import kotlinx.coroutines.delay

@SuppressLint("ProduceStateDoesNotAssignValue")
@Composable
fun WhackAMoleGame(
    navController: NavHostController ,
    modifier: Modifier = Modifier,
    context: Context,
    token: String,
    wamViewModel: WAMViewModel,
    homeViewModel: HomeViewModel,
    gameMode: String
) {

//    //gridsize 4x4 for intense
//    val gridSize = when (gameMode) {
//        "intense" -> 4
//        else -> 3
//    }

//    //waktu lebih cepat untuk intense
//    val moleAppearanceDelay = when (gameMode) {
//        "intense" -> 500L
//        else -> 1000L
//    }

//    //check if it's timed(/intense)
//    val isTimedMode = gameMode in listOf("timed", "intense") // Check if the mode is timed

    LaunchedEffect(Unit) {wamViewModel.startGame()}

//    //Mole appearing
//    LaunchedEffect (Unit) {
//        while (true && timeRemaining != 0) {
//            //mole appearing w delay
//            delay(wamViewModel.moleAppearanceDelay)
//            //after delay mole muncul
//            activeMole = (0 until gridSize * gridSize).random()
//        }
//    }
//
//    //countdown (use int then delay 1000ms)
//    LaunchedEffect(isTimedMode) {
//        if (isTimedMode) {
//            while (timeRemaining > 0) {
//                delay(1000L)
//                timeRemaining--
//            }
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display game information
        Text(text = "Mode: ${wamViewModel.mode}")
        Text(text = "Score: ${wamViewModel.score}")
        if (wamViewModel.isTimedMode) {
            Text(text = "Time Remaining: ${wamViewModel.timeRemaining}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Game grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(wamViewModel.gridSize),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(wamViewModel.gridSize * wamViewModel.gridSize) { index ->
                Button(
                    onClick = { wamViewModel.onMoleClick(index) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (wamViewModel.isMoleAtIndex(index)) Color.Green else Color.Gray
                    ),
                    modifier = Modifier
                        .aspectRatio(1f)
                ) {}
            }
        }

        if(!wamViewModel.isTimedMode){
            Button(
                onClick = { wamViewModel.gameOver() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Stop Game")
            }
        }

        if (wamViewModel.gameIsOver) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Game Over! Final Score: ${wamViewModel.score}", color = Color.Red)
            Text(text = "Highscore: ${wamViewModel.highscore}")
            Text(text = "Mode: ${wamViewModel.timed_highscore}")
            Text(text = "Mode: ${wamViewModel.endless_highscore}")
            Text(text = "Mode: ${wamViewModel.intense_highscore}")
            //popbackstack is app screen rn is removed then muncul screen before
            Button(onClick = { wamViewModel.backToMenu(token, navController, {homeViewModel.getWAMData(token, navController)}) }) {
                Text(text = "Return to Menu")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WhackAMoleViewPreview() {
    WhackAMoleGame( navController = rememberNavController(), gameMode = "endless", wamViewModel = WAMViewModel(TODO()), context = TODO(), homeViewModel = TODO(), token = TODO())
}