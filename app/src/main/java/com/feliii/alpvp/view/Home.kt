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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.feliii.alpvp.R
import com.feliii.alpvp.enums.PagesEnum
import com.feliii.alpvp.uiStates.StringDataStatusUIState
import com.feliii.alpvp.viewmodel.AuthenticationViewModel
import com.feliii.alpvp.viewmodel.CalendarViewModel
import com.feliii.alpvp.viewmodel.HomeViewModel
import com.feliii.alpvp.viewmodel.WAMViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun mainMenu(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    calendarViewModel: CalendarViewModel,
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
// }

//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun CalendarPreview() {
//    mainMenu()
//}