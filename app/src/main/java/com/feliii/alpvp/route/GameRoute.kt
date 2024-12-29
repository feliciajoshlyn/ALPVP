package com.feliii.alpvp.route

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.feliii.alpvp.enums.PagesEnum
import com.feliii.alpvp.view.WhackAMoleMenu
import com.feliii.alpvp.viewmodel.WAMViewModel

import com.feliii.alpvp.view.login
import com.feliii.alpvp.view.register
import com.feliii.alpvp.viewmodel.AuthenticationViewModel
import com.feliii.alpvp.viewmodel.HomeViewModel

@Composable
fun RelaxGameApp(
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
    authenticationViewModel: AuthenticationViewModel = viewModel(factory = AuthenticationViewModel.Factory)
    wamViewModel: WAMViewModel = viewModel(factory = WAMViewModel.Factory),
){
    val localContext = LocalContext.current
    val token = homeViewModel.token.collectAsState()

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
        composable(route = PagesEnum.WhackAMoleMenu.name) {
            WhackAMoleMenu(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                navController = navController,
                context = localContext,
                wamViewModel = wamViewModel,
                token = token.value
            )
        }
    }
}