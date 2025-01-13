package com.feliii.alpvp.view

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feliii.alpvp.R
import com.feliii.alpvp.enums.PagesEnum
import com.feliii.alpvp.uiStates.StringDataStatusUIState
import com.feliii.alpvp.viewmodel.CalendarViewModel
import com.feliii.alpvp.viewmodel.HomeViewModel
import com.feliii.alpvp.viewmodel.ProfileViewModel
import com.feliii.alpvp.viewmodel.WAMViewModel

@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    navController: NavHostController,
    token: String,
    context: Context,
) {
    val logoutStatus = profileViewModel.logoutStatus
    val selectedSong = profileViewModel.selectedSong.collectAsState().value
    val songList = profileViewModel.getSongList()

    LaunchedEffect(logoutStatus) {
        if (logoutStatus is StringDataStatusUIState.Failed) {
            Toast.makeText(
                context,
                "LOGOUT ERROR: ${logoutStatus.errorMessage}",
                Toast.LENGTH_SHORT
            ).show()
            profileViewModel.clearLogoutErrorMessage()
        }
    }

    if (logoutStatus is StringDataStatusUIState.Loading) {
        // Show a loading indicator if needed
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF5E4890))
                .padding(top = 100.dp, start = 20.dp, end = 20.dp),
        ) {
            // Title
            Text(
                text = "Profile",
                fontSize = 40.sp,
                color = Color(0xFF5E4890),
                fontFamily = FontFamily(Font(R.font.jua)),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .align(Alignment.CenterHorizontally)
                    .background(Color(0xFFD7C4EC))
                    .padding(20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Hi ${profileViewModel.username.collectAsState().value}",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.jua)),
                color = Color(0xFFD7C4EC)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Select Background Music:",
                fontSize = 20.sp,
                color = Color(0xFFD7C4EC),
                fontFamily = FontFamily(Font(R.font.jua))
            )

            Spacer(modifier = Modifier.height(16.dp))

            songList.forEach { song ->
                Text(
                    text = song.name,
                    fontSize = 18.sp,
                    color = if (song == selectedSong) Color.Green else Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            profileViewModel.setSelectedSong(song.name, context)
                        }
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD7C4EC)),
                onClick = {
                    profileViewModel.stopMusic() // Stop music before logging out
                    profileViewModel.logoutUser(token, navController)
                }
            ) {
                Text(
                    text = "Logout",
                    fontFamily = FontFamily(Font(R.font.jua)),
                    fontSize = 20.sp
                )
            }
        }
    }
}




//    Button(
//        onClick = { wamViewModel.getWAMData(token = token, navController = navController) },
//    ) {
//        Text(
//            text = "whack a mole menu"
//        )
//    }
//
//    Button(
//        onClick = { homeViewModel.logoutUser(token, navController) }
//    ) {
//        Text(
//            text = "logout"
//        )
//    }
//}

//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun CalendarPreview() {
//    mainMenu()
//}