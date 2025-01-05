package com.feliii.alpvp.view

import android.content.Context
import android.media.MediaPlayer
import android.widget.Button
import com.feliii.alpvp.R
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feliii.alpvp.enums.PagesEnum
import com.feliii.alpvp.uiStates.WAMDataStatusUIState
import com.feliii.alpvp.viewmodel.HomeViewModel
import com.feliii.alpvp.viewmodel.WAMViewModel
import kotlinx.coroutines.delay

@Composable
fun WhackAMoleMenu(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    wamViewModel: WAMViewModel,
    navController: NavHostController,
    token: String,
    context: Context
) {
    LaunchedEffect(Unit) {
        wamViewModel.startSong(context)
    }


    val dataStatus = homeViewModel.wamDataStatus


    when (dataStatus) {
        is WAMDataStatusUIState.Success -> {
            wamViewModel.setChosenSong(dataStatus.data.song_chosen)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF85BB65))
                    .padding(horizontal = 30.dp)
                    .padding(top = 40.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                //backbutton
                Button(
                    onClick = {
                        wamViewModel.leaveMenu()
                        navController.navigate(PagesEnum.Home.name)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E4890)),
                    modifier = Modifier.align(Alignment.Start)
                        .padding(bottom = 60.dp)
                ){
                    Text(
                        text = "Back",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.jua)),
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 6.dp, shape = RoundedCornerShape(12.dp))
                        .background(Color(0xFFD7C4EC), shape = RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Whack-A-Mole",
                        fontFamily = FontFamily(Font(R.font.jua)),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5E4890),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Spacer for pushing buttons to the middle
                Spacer(modifier = Modifier.weight(1f))

                // Buttons Column
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val buttonModifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(50.dp)
                        .shadow(elevation = 6.dp, shape = RoundedCornerShape(12.dp))

                    // Timed Mode Button
                    Button(
                        modifier = buttonModifier,
                        onClick = {
                            wamViewModel.navigateToGame(
                                navController,
                                "timed",
                                dataStatus.data
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E4890)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Timed Mode",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.jua))
                            )
                            Text(
                                text = dataStatus.data.timed_highscore.toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.jua))
                            )
                        }
                    }

                    // Endless Mode Button
                    Button(
                        modifier = buttonModifier,
                        onClick = {
                            wamViewModel.navigateToGame(
                                navController,
                                "endless",
                                dataStatus.data
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E4890)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Endless Mode",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.jua))
                            )
                            Text(
                                text = dataStatus.data.endless_highscore.toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.jua))
                            )
                        }
                    }

                    Button(
                        modifier = buttonModifier,
                        onClick = {
                            wamViewModel.navigateToGame(
                                navController,
                                "intense",
                                dataStatus.data
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E4890)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Intense Mode",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.jua))
                            )
                            Text(
                                text = dataStatus.data.intense_highscore.toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.jua))
                            )
                        }
                    }
                }

                // Spacer for pushing the MusicPlayerBar to the bottom
                Spacer(modifier = Modifier.weight(1f))

                // Music Player Bar at the bottom
                MusicPlayerBar(
                    wamViewModel = wamViewModel,
                    context = context
                )
            }
        }
            else -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No Data Found!",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5E4890)
                )
            }
        }
    }
}





@Composable
@Preview(showBackground = true, showSystemUi = true)
fun WhackAMoleMenuPreview() {
    WhackAMoleMenu(
        modifier = TODO(),
        wamViewModel = TODO(),
        navController = TODO(),
        token = TODO(),
        context = TODO(),
        homeViewModel = TODO()
    )
}