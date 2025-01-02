package com.feliii.alpvp.view


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
){
    Scaffold (
        bottomBar = {
            BottomAppBar (
                modifier = Modifier.height(96.dp),
                containerColor = Color(0xFF8871CA) //Blue Marguerite
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ){
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
                                /* logic here... */
                            }
                            .padding(2.dp)
                    )

                    // Mood Calendar bottom navbar
                    Image(
                        painter = painterResource(R.drawable.calendar_month),
                        contentDescription = "calendar",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(48.dp)
                            .clickable {
                                /* logic here... */
                            }
                            .padding(2.dp)
                    )
                }
            }
        }
    ){ innerpadding ->
        Box (
            modifier = Modifier.fillMaxSize()
                .background(Color(0xFF5E4890))
                .padding(innerpadding)
        ){
            Column (
                modifier = Modifier.background(Color(0xFF5E4890))
            ){

            }
        }
    }

    Button(
        onClick = { wamViewModel.getWAMData(token = token, navController = navController) },
    ) {
        Text(
            text = "whack a mole menu"
        )
    }
    
    Button(
        onClick = { homeViewModel.logoutUser(token, navController) }
    ) {
        Text(
            text = "logout"
        )
    }
}

//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun CalendarPreview() {
//    mainMenu()
//}