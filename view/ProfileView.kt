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

    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF5E4890))
                .padding(top = 100.dp)
                .padding(horizontal = 20.dp),
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
            Column {
                Text(
                    text = "Hi ${profileViewModel.username.collectAsState().value}",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.jua)),
                    color = Color(0xFFD7C4EC),
                    modifier = Modifier
                        .padding(top = 20.dp)
                )

            }

            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 200.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD7C4EC)),
                onClick = {
                    profileViewModel.logoutUser(token, navController)
                },
            ) {
                Text(
                    text = "logout",
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