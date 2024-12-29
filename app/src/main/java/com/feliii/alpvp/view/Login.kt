package com.feliii.alpvp.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.feliii.alpvp.uiStates.AuthenticationStatusUIState
import com.feliii.alpvp.viewmodel.AuthenticationViewModel

@Composable
fun login(
    authenticationViewModel: AuthenticationViewModel = viewModel (),
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current
){
    val loginUIState by authenticationViewModel.authenticationUIState.collectAsState()

    LaunchedEffect(authenticationViewModel.dataStatus) {
        val dataStatus = authenticationViewModel.dataStatus
        if (dataStatus is AuthenticationStatusUIState.Failed) {
            Toast.makeText(context, dataStatus.errorMessage, Toast.LENGTH_SHORT).show()
            authenticationViewModel.clearErrorMessage()
        }
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(24.dp),
    ){
        Text(
            text = "Login",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD7C4EC),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Box(
            modifier = Modifier
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                .background(Color(0xFFD7C4EC))
        ){
            Column (
                modifier = Modifier.padding(20.dp)
            ){
                TextField(

                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = "username")
                    },
                    value = authenticationViewModel.usernameInput,
                    onValueChange = {
                        authenticationViewModel.changeUsernameInput(it)
                        authenticationViewModel.checkLoginForm()
                                    },
                    label ={ Text(text = "Username")},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                TextField(
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "password")
                    },
                    value = authenticationViewModel.passwordInput,
                    onValueChange = {authenticationViewModel.changePasswordInput(it)},
                    label ={ Text(text = "Password")},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = loginUIState.passwordVisibility
                )
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Text(
                        text = "Don't have an account yet? ",
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                    )
                    ClickableText (
                        text = AnnotatedString("Register"),
                        onClick = {
                            // Handle the click and navigate to the register page
                            // For example, call your navigation function here
                        },
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF0066CC), // Blue color for the "Register" link
                            fontWeight = FontWeight.Bold
                        ),
                    )
                }

                Button(
                    onClick = { authenticationViewModel.loginUser(navController = navController) },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Login")
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun loginPreview(){
    login()
}