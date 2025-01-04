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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.feliii.alpvp.viewmodel.HomeViewModel
import com.feliii.alpvp.viewmodel.WAMViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun mainMenu(
    modifier: Modifier = Modifier,
    wamViewModel: WAMViewModel,
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    token: String,
    username: String, //nnti delete
    context: Context
) {
    val logoutStatus = homeViewModel.logoutStatus

    LaunchedEffect(logoutStatus) {
        if (logoutStatus is StringDataStatusUIState.Failed) {
            Toast.makeText(
                context,
                "LOGOUT ERROR: ${logoutStatus.errorMessage}",
                Toast.LENGTH_SHORT
            ).show()
            homeViewModel.clearLogoutErrorMessage()
        }
    }

    if (logoutStatus is StringDataStatusUIState.Loading) {

    } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF5E4890))
            ) {
                // Title
                Text(
                    text = "Mini Games",
                    fontSize = 40.sp,
                    color = Color(0xFF5E4890),
                    fontFamily = FontFamily(Font(R.font.jua)),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = 110.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFD7C4EC))
                        .padding(20.dp)
                )

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(44.dp))

                    // Row of <Whack a Mole> and <Fidget Spinner>
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        // Whack a Mole
                        Column (
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFD7C4EC))
                                .padding(20.dp)
                                .clickable {
                                    navController.navigate(PagesEnum.WhackAMoleMenu) {
                                        popUpTo(PagesEnum.WhackAMoleMenu.name) {
                                            inclusive = true
                                        }
                                    }
                                }
                        ){
                            Image(
                                painter = painterResource(R.drawable.whac_a_mole),
                                contentDescription = "whack a mole",
                                modifier = Modifier.size(100.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Whack\na Mole",
                                fontSize = 24.sp,
                                color = Color(0xFF5E4890),
                                fontFamily = FontFamily(Font(R.font.jua)),
                                textAlign = TextAlign.Center
                            )
                        }

                        // Fidget Spinner
                        Column (
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFD7C4EC))
                                .padding(20.dp)
                                .clickable {
                                    // sementara ke WAM
                                    navController.navigate(PagesEnum.WhackAMoleMenu) {
                                        popUpTo(PagesEnum.WhackAMoleMenu.name) {
                                            inclusive = true
                                        }
                                    }
                                }
                        ){
                            Image(
                                painter = painterResource(R.drawable.pinwheel),
                                contentDescription = "fidget spinner",
                                modifier = Modifier.size(100.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Fidget\nSpinner",
                                fontSize = 24.sp,
                                color = Color(0xFF5E4890),
                                fontFamily = FontFamily(Font(R.font.jua)),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))

                    // Row of <Cookie Clicker> and <Leaf Fall>
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        // Cookie Clicker
                        Column (
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFD7C4EC))
                                .padding(20.dp)
                                .clickable {
                                    // sementara ke WAM
                                    navController.navigate(PagesEnum.WhackAMoleMenu) {
                                        popUpTo(PagesEnum.WhackAMoleMenu.name) {
                                            inclusive = true
                                        }
                                    }
                                }
                        ){
                            Image(
                                painter = painterResource(R.drawable.cookie_logo),
                                contentDescription = "fidget spinner",
                                modifier = Modifier.size(100.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Cookie\nClicker",
                                fontSize = 24.sp,
                                color = Color(0xFF5E4890),
                                fontFamily = FontFamily(Font(R.font.jua)),
                                textAlign = TextAlign.Center
                            )
                        }

                        // Leaf Fall
                        Column (
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFD7C4EC))
                                .padding(20.dp)
                                .clickable {
                                    // sementara ke WAM
                                    navController.navigate(PagesEnum.WhackAMoleMenu) {
                                        popUpTo(PagesEnum.WhackAMoleMenu.name) {
                                            inclusive = true
                                        }
                                    }
                                }
                        ){
                            Image(
                                painter = painterResource(R.drawable.leaf_fall_logo),
                                contentDescription = "whack a mole",
                                modifier = Modifier.size(100.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Leaf\nFall",
                                fontSize = 24.sp,
                                color = Color(0xFF5E4890),
                                fontFamily = FontFamily(Font(R.font.jua)),
                                textAlign = TextAlign.Center
                            )
                        }

                    }

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