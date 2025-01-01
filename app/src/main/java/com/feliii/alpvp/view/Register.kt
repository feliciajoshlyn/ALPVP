package com.feliii.alpvp.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.feliii.alpvp.R
import com.feliii.alpvp.enums.PagesEnum
import com.feliii.alpvp.viewmodel.AuthenticationViewModel

@Composable
fun register(
    authenticationViewModel: AuthenticationViewModel = viewModel (),
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }


    // this Box is the background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5E4890))
    ) {
        // bg Cat image
        Image(
            painter = painterResource(R.drawable.happy_cat),
            contentDescription = null,
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.TopStart)
                .offset(x = -85.dp, y = -110.dp)
                .graphicsLayer(
                    rotationZ = -23f, // Tlit
                    alpha = 0.2f     // transparancy
                )
        )

        // bg Flower image top end
        Image(
            painter = painterResource(R.drawable.sketchy_flower),
            contentDescription = "flower",
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.TopEnd)
                .offset(x = 150.dp, y = -10.dp)
                .graphicsLayer(
                    alpha = 0.3f
                )
                .padding(16.dp)
        )
        // bg Flower image bottom end
        Image(
            painter = painterResource(R.drawable.sketchy_flower),
            contentDescription = "flower",
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 85.dp, y = -50.dp)
                .graphicsLayer(
                    alpha = 0.3f
                )
                .padding(16.dp)
        )
        // bg Flower image bottom start
        Image(
            painter = painterResource(R.drawable.sketchy_flower),
            contentDescription = "flower",
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.BottomStart)
                .offset(x = -140.dp, y = 100.dp)
                .graphicsLayer(
                    alpha = 0.3f
                )
                .padding(16.dp)
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(24.dp),
        ) {
            Text(
                text = "Register",
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD7C4EC),
                fontFamily = FontFamily(Font(R.font.jua)),
            )
            Spacer(modifier = Modifier.padding(bottom = 28.dp))

            Box(
                modifier = Modifier
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                    .background(Color(0xFFD7C4EC))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Input Username
                    TextField(
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = "username")
                        },
                        value = username,
                        onValueChange = { username = it },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0XFFF6EDFF),
                            unfocusedContainerColor = Color(0XFFFAF4FF),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        label = {
                            Text(
                                text = "Username",
                                color = Color(0xFF5445AC),
                                fontFamily = FontFamily(Font(R.font.jua)),
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.clip(RoundedCornerShape(10.dp)).fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.padding(8.dp))

                    // input Password
                    TextField(
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = "password")
                        },
                        value = password,
                        onValueChange = { password = it },
                        label = {
                            Text(
                                text = "Password",
                                color = Color(0xFF5445AC),
                                fontFamily = FontFamily(Font(R.font.jua)),
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0XFFF6EDFF),
                            unfocusedContainerColor = Color(0XFFFAF4FF),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.clip(RoundedCornerShape(10.dp)).fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.padding(8.dp))

                    // input Confirm Password
                    TextField(
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = "password")
                        },
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = {
                            Text(
                                text = "Confirm Password",
                                color = Color(0xFF5445AC),
                                fontFamily = FontFamily(Font(R.font.jua)),
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0XFFF6EDFF),
                            unfocusedContainerColor = Color(0XFFFAF4FF),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.clip(RoundedCornerShape(10.dp)).fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.padding(4.dp))

                    // Have an Account? Login
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Have an Account? ",
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.jua)),
                            color = Color(0xFF5445AC),
                            fontSize = 14.sp
                        )
                        ClickableText(
                            text = AnnotatedString("Login"),
                            onClick = {
                                // Handle the click and navigate to the register page
                                authenticationViewModel.resetViewModel()
                                navController.navigate(PagesEnum.Login.name) {
                                    popUpTo(PagesEnum.Register.name) {
                                        inclusive = true
                                    }
                                }
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF0066CC),
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.jua)),
                                fontSize = 14.sp
                            ),
                        )
                    }
                    Spacer(modifier = Modifier.padding(bottom = 12.dp))

                    // POST button
                    Button(
                        onClick = { authenticationViewModel.registerUser(navController = navController) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF9141E6),
                            disabledContainerColor = Color.Gray, // Background color when disabled
                            disabledContentColor = Color.LightGray // Text/Icon color when disabled
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Register",
                            fontFamily = FontFamily(Font(R.font.jua)),
                            fontSize = 16.sp
                        )
                    }
                }
            }

        }

    }

}

//@Composable
//@Preview(showBackground = true, showSystemUi = true)
//fun registerPreview(){
//    register()
//}