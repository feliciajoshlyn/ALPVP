package com.feliii.alpvp.view

import android.content.Context
import android.media.MediaPlayer
import com.feliii.alpvp.R
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feliii.alpvp.uiStates.WAMDataStatusUIState
import com.feliii.alpvp.viewmodel.HomeViewModel
import com.feliii.alpvp.viewmodel.WAMViewModel

@Composable
fun WhackAMoleMenu(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    wamViewModel: WAMViewModel,
    navController: NavHostController,
    token: String,
    context: Context
) {
    val context = LocalContext.current
    var isPlaying = remember { mutableStateOf(false) }
    val song = remember { MediaPlayer.create(context, R.raw.lofi) }

    val dataStatus = homeViewModel.wamDataStatus

    when(dataStatus){
        is WAMDataStatusUIState.Success ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Text at the top
            Text(
                text = "Whack-A-Mole",
                modifier = Modifier
                    .padding(
                        top = 64.dp,
                        bottom = 270.dp
                    ) // Adjust the padding for spacing from the top
            )
            // Buttons in the middle
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { wamViewModel.navigateToGame(navController, "timed", dataStatus.data) },
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Timed Mode")
                        Text(text = dataStatus.data.timed_highscore.toString())
                    }
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {wamViewModel.navigateToGame(navController, "endless", dataStatus.data)},
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Endless Mode")
                        Text(text = dataStatus.data.endless_highscore.toString())
                    }
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {wamViewModel.navigateToGame(navController, "intense", dataStatus.data)},
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Intense Mode")
                        Text(text = dataStatus.data.intense_highscore.toString())
                    }
                }
                Button(
                    onClick = {
                        isPlaying.value = !isPlaying.value; if (isPlaying.value) {
                        song.start()
                    } else {
                        song.pause()
                    }
                    }
                ) {
                    Text(
                        text = "play"
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Push content up from the bottom
        }
        else -> Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No Data Found!",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
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