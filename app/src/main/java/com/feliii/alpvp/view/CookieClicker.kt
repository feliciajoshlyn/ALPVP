package com.feliii.alpvp.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feliii.alpvp.R
import com.feliii.alpvp.viewmodel.CookieClickerViewModel

@Composable
fun CookieClicker(
    navController: NavHostController,
    viewModel: CookieClickerViewModel
) {
    val context = LocalContext.current
    val cookies by viewModel.totalCookies.collectAsState()
    val upgradePower by viewModel.upgradePower.collectAsState()
    val currentSong by viewModel.currentSong.collectAsState()

    var isShopOpen by remember { mutableStateOf(false) }
    var isSettingsOpen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.startMusic(context) // Start music when the screen loads
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5E4890))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Cookies: $cookies",
                fontSize = 24.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.cookie_logo),
                contentDescription = "Cookie",
                modifier = Modifier
                    .size(200.dp)
                    .clickable {
                        viewModel.clickCookie()
                    }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { isShopOpen = true }) {
                Text(text = "Shop")
            }
        }

        Icon(
            painter = painterResource(id = R.drawable.baseline_settings_24),
            contentDescription = "Settings",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(40.dp)
                .clickable { isSettingsOpen = true },
            tint = Color.White
        )

        if (isShopOpen) {
            ShopDialog(
                cookies = cookies,
                onDismiss = { isShopOpen = false },
                onUpgradeClickPower = { cost -> viewModel.upgradeClickPower(cost) }
            )
        }

        if (isSettingsOpen) {
            CookieClickerSettingsDialog(
                onDismiss = { isSettingsOpen = false },
                currentSong = currentSong.name,
                navController = navController,
                onSongChange = { _, song -> viewModel.setChosenSong(song, context) }
            )
        }
    }
}


@Composable
fun ShopDialog(
    cookies: Int,
    onDismiss: () -> Unit,
    onUpgradeClickPower: (Int) -> Unit // Accept cost
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Shop") },
        text = {
            Column {
                Button(onClick = {
                    if (cookies >= 10) {
                        onUpgradeClickPower(10) // Pass the cost here
                    } else {
                        Toast.makeText(
                            context,
                            "Not enough cookies!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }) {
                    Text(text = "Upgrade Click Power (+1) - 10 Cookies")
                }
            }
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("Close")
            }
        }
    )
}



@Composable
fun CookieClickerSettingsDialog(
    onDismiss: () -> Unit,
    currentSong: String,
    navController: NavHostController,
    onSongChange: (Context, String) -> Unit
) {
    val context = LocalContext.current
    val songList = listOf("Sugar Dream", "Caramel Clouds", "Choco Chill", "Vanilla")

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Settings") },
        text = {
            Column {
                Text(text = "Current Song: $currentSong", color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Change Song:")
                Spacer(modifier = Modifier.height(8.dp))

                songList.forEach { song ->
                    Button(
                        onClick = { onSongChange(context, song) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = song)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                navController.navigate("Home") {
                    popUpTo("CookieClicker.name") { inclusive = true }
                }
            }) {
                Text("Back to Main Menu")
            }
        }
    )
}
