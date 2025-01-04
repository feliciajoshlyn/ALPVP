package com.feliii.alpvp.route

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.feliii.alpvp.R
import com.feliii.alpvp.enums.PagesEnum
import com.feliii.alpvp.view.MoodCalendar
import com.feliii.alpvp.view.TodayMood
import com.feliii.alpvp.view.WhackAMoleGame
import com.feliii.alpvp.view.WhackAMoleMenu
import com.feliii.alpvp.viewmodel.WAMViewModel
import com.feliii.alpvp.view.login
import com.feliii.alpvp.view.mainMenu
import com.feliii.alpvp.view.register
import com.feliii.alpvp.viewmodel.AuthenticationViewModel
import com.feliii.alpvp.viewmodel.CalendarDetailViewModel
import com.feliii.alpvp.viewmodel.CalendarViewModel
import com.feliii.alpvp.viewmodel.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RelaxGameApp(
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
    authenticationViewModel: AuthenticationViewModel = viewModel(factory = AuthenticationViewModel.Factory),
    wamViewModel: WAMViewModel = viewModel(factory = WAMViewModel.Factory),
    calendarViewModel: CalendarViewModel = viewModel(factory = CalendarViewModel.Factory),
    calendarDetailViewModel: CalendarDetailViewModel = viewModel(factory = CalendarDetailViewModel.Factory)
){
    val localContext = LocalContext.current
    val token = homeViewModel.token.collectAsState()
    val username = homeViewModel.username.collectAsState()

    NavHost(navController = navController, startDestination = if(token.value != "Unknown" && token.value != ""){
        PagesEnum.Home.name
    } else {
        PagesEnum.Login.name
    }) {
        composable(route = PagesEnum.Login.name) {
            login(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                authenticationViewModel = authenticationViewModel,
                navController = navController,
                context = localContext
            )
        }

        composable(route = PagesEnum.Register.name) {
            register(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                authenticationViewModel = authenticationViewModel,
                navController = navController,
                context = localContext
            )
        }

        composable(route = PagesEnum.Home.name) {
            Scaffold(
                bottomBar = {
                    BottomAppBar(
                        modifier = Modifier.height(96.dp),
                        containerColor = Color(0xFF8871CA) //Blue Marguerite
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // User bottom navbar
                            Image(
                                painter = painterResource(R.drawable.user_personoutline),
                                contentDescription = "user",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(48.dp)
                                    .clickable {
                                        /* logic here... */
                                    }
                                    .padding(2.dp)
                            )

                            // Minigame bottom navbar
                            Image(
                                painter = painterResource(R.drawable.minigame_window),
                                contentDescription = "Minigame",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFF644E96))
                                    .size(72.dp)
                                    .clickable {
                                        /* logic here... */
                                    }
                                    .padding(8.dp)
                            )

                            // Mood Calendar bottom navbar
                            Image(
                                painter = painterResource(R.drawable.calendar_month),
                                contentDescription = "calendar",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(48.dp)
                                    .clickable {
                                        navController.navigate(PagesEnum.Calendar.name) {
                                            popUpTo(PagesEnum.Calendar.name) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                    .padding(2.dp)
                            )
                        }
                    }
                }
            ) { innerpadding ->
                mainMenu(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .padding(innerpadding),
                    navController = navController,
                    context = localContext,
                    token = token.value,
                    username = username.value,
                    homeViewModel = homeViewModel,
                    calendarViewModel = calendarViewModel,
                    wamViewModel = wamViewModel,
                )
            }
        }
        
        composable(route = PagesEnum.WhackAMoleMenu.name) {
            WhackAMoleMenu(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                navController = navController,
                context = localContext,
                wamViewModel = wamViewModel,
                homeViewModel = homeViewModel,
                token = token.value
            )
        }
        composable(route = PagesEnum.WhackAMoleGame.name) {
            WhackAMoleGame(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                navController = navController,
                context = localContext,
                wamViewModel = wamViewModel,
                token = token.value,
                homeViewModel = homeViewModel,
                gameMode = ""
            )
        }

        composable(route = PagesEnum.Calendar.name) {
            Scaffold(
                bottomBar = {
                    BottomAppBar(
                        modifier = Modifier.height(96.dp),
                        containerColor = Color(0xFF8871CA) //Blue Marguerite
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // User bottom navbar
                            Image(
                                painter = painterResource(R.drawable.user_personoutline),
                                contentDescription = "user",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(48.dp)
                                    .clickable {
                                        /* logic here... */
                                    }
                                    .padding(2.dp)
                            )

                            // Minigame bottom navbar
                            Image(
                                painter = painterResource(R.drawable.minigame_window),
                                contentDescription = "Minigame",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(72.dp)
                                    .clickable {
                                        navController.navigate(PagesEnum.Home.name) {
                                            popUpTo(PagesEnum.Home.name) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                    .padding(2.dp)
                            )

                            // Mood Calendar bottom navbar
                            Image(
                                painter = painterResource(R.drawable.calendar_month),
                                contentDescription = "calendar",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFF644E96))
                                    .size(48.dp)
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            ) { innerpadding ->
                MoodCalendar(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerpadding),
                    navController = navController,
                    context = localContext,
                    calendarViewModel = calendarViewModel,
                    calendarDetailViewModel = calendarDetailViewModel,
                    token = token.value
                )
            }
        }

        composable(route = PagesEnum.TodayMood.name) {
            TodayMood(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                navController = navController,
                context = localContext,
                calendarDetailViewModel = calendarDetailViewModel,
                token = token.value
            )
        }
    }
}