package com.feliii.alpvp.view

@Composable
fun mainMenu(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    calendarViewModel: CalendarViewModel,
    navController: NavHostController,
    token: String,
    username: String, //nnti delete
    context: Context
){

    val logoutStatus = homeViewModel.logoutStatus

    LaunchedEffect(logoutStatus) {
        if (logoutStatus is StringDataStatusUIState.Failed) {
            Toast.makeText(context, "LOGOUT ERROR: ${logoutStatus.errorMessage}", Toast.LENGTH_SHORT).show()
            homeViewModel.clearLogoutErrorMessage()
        }
    }

    if(logoutStatus is StringDataStatusUIState.Loading){

    }else{
        Column {
            Button(
                onClick = {homeViewModel.getWAMData(token = token, navController = navController)},
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
                onClick = {calendarViewModel.getCalendarData(token = token, navController = navController)}
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
    }

}
