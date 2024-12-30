package com.feliii.alpvp.view

import android.content.Context
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.feliii.alpvp.viewmodel.AuthenticationViewModel
import com.feliii.alpvp.viewmodel.HomeViewModel
import com.feliii.alpvp.viewmodel.WAMViewModel

@Composable
fun mainMenu(
    modifier: Modifier = Modifier,
    wamViewModel: WAMViewModel,
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    token: String,
    username: String, //nnti delete
    context: Context
){
    Button(
        onClick = {wamViewModel.getWAMData(token = token, navController = navController)},
    ) {
        Text(
            text = "whack a mole menu"
        )
        Text(
            text = token
        )
        Text(
            text = username
        )
    }
    Button(
        onClick = {}
    ) {
        Text(
            text = "calendar"
        )
    }
    Button(
        onClick = {homeViewModel.logoutUser(token, navController)}
    ) {
        Text(
            text = "logout"
        )
    }
}