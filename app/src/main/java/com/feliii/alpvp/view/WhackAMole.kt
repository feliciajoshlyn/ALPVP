package com.feliii.alpvp.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.feliii.alpvp.R
import com.feliii.alpvp.viewmodel.HomeViewModel
import com.feliii.alpvp.viewmodel.WAMViewModel
import kotlinx.coroutines.delay

@SuppressLint("ProduceStateDoesNotAssignValue")
@Composable
fun WhackAMoleGame(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    context: Context,
    token: String,
    wamViewModel: WAMViewModel,
    homeViewModel: HomeViewModel,
) {
    LaunchedEffect(Unit) {
        wamViewModel.startGame()
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF85BB65))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //mode
            Box(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .background(Color(0xFFD7C4EC), shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 8.dp, horizontal = 50.dp)
            ) {
                Text(
                    text = wamViewModel.mode.capitalize(),
                    fontFamily = FontFamily(Font(R.font.jua)),
                    color = Color(0xFF5E4890),
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.Center)
                        .padding(vertical = 5.dp)
                )
            }

            //score
            Box(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .background(Color(0xFFD7C4EC), shape = RoundedCornerShape(12.dp))
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(12.dp))
                    .height(100.dp)
                    .width(100.dp)
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = "${wamViewModel.score}",
                    fontFamily = FontFamily(Font(R.font.jua)),
                    color = Color(0xFF5E4890),
                    fontSize = 30.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            //timer
            if (wamViewModel.isTimedMode) {
                Text(
                    text = "Time Remaining: ${wamViewModel.timeRemaining}",
                    fontFamily = FontFamily(Font(R.font.jua)),
                    color = Color(0xFF5E4890),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            //mole holes
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
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF80471C)),
                        modifier = Modifier.aspectRatio(1f)
                    ) {
                        if (wamViewModel.isMoleAtIndex(index)) {
                            Image(
                                painter = painterResource(wamViewModel.mole_image_id),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            //stop button
            if (!wamViewModel.isTimedMode) {
                Button(
                    onClick = { wamViewModel.gameOver() },
                    modifier = Modifier.padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E4890)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Stop Game",
                        fontFamily = FontFamily(Font(R.font.jua)),
                        color = Color.White
                    )
                }
            }
        }

        //game over
        if (wamViewModel.gameIsOver) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .background(Color(0xFFD7C4EC), shape = RoundedCornerShape(12.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Game Over!",
                        color = Color.Red,
                        fontFamily = FontFamily(Font(R.font.jua)),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Final Score: ${wamViewModel.score}",
                        fontFamily = FontFamily(Font(R.font.jua)),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Highscore: ${wamViewModel.highscore}",
                        fontFamily = FontFamily(Font(R.font.jua)),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(
                        onClick = {
                        wamViewModel.backToMenu(
                            token,
                            navController
                        ) { homeViewModel.getWAMData(token, navController) }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E4890)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = "Return to Menu",
                            fontFamily = FontFamily(Font(R.font.jua)),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WhackAMoleViewPreview() {
    WhackAMoleGame(
        navController = rememberNavController(),
        wamViewModel = WAMViewModel(TODO()),
        context = TODO(),
        homeViewModel = TODO(),
        token = TODO()
    )
}
