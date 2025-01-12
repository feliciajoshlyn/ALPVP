package com.feliii.alpvp.view

import android.annotation.SuppressLint
import android.content.Context
import android.icu.number.Scale
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.feliii.alpvp.R
import com.feliii.alpvp.enums.PagesEnum
import com.feliii.alpvp.uiStates.CalendarDetailDataStatusUIState
import com.feliii.alpvp.uiStates.FSDataStatusUIState
import com.feliii.alpvp.viewmodel.FidgetSpinnerViewModel
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.hypot

@Composable
fun FidgetSpinner(
    token: String,
    context: Context,
    navController: NavHostController,
    fsViewModel: FidgetSpinnerViewModel
) {
    val fsUIState by fsViewModel.fsUIState.collectAsState()

    LaunchedEffect(fsViewModel.fsDataStatus) {
        val dataStatus = fsViewModel.fsDataStatus
        // Display error if data fetching fails
        if(dataStatus is FSDataStatusUIState.Failed) {
            Toast.makeText(context, dataStatus.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    val coroutineScope = rememberCoroutineScope()
    var rotation = fsViewModel.rotation

    // unit = void?
    LaunchedEffect(Unit) {
        fsViewModel.getFSData(token)
        fsViewModel.startSong(context)
    }

    val stateZ = rememberTransformableState { _, _, rotationChange ->
        coroutineScope.launch {
            fsViewModel.updateRotation(rotationChange)
        }
    }


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .paint( //Background
                painter = painterResource(fsViewModel.backgroundImageResource),
                alignment = Alignment.Center,
                contentScale = ContentScale.FillBounds
            )
    ) {
        // Back item image
        if (fsViewModel.backItemImageResource != 0){
            Image(
                painter = painterResource(fsViewModel.backItemImageResource),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .offset(
                        x = fsViewModel.offsetX,
                        y = fsViewModel.offsetY
                    )
            )
        }

        // Setting change
        Column(
            horizontalAlignment = AbsoluteAlignment.Right,
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.TopEnd)
                .background(Color(0xFFD7C4EC), shape = RoundedCornerShape(20.dp))
                .padding(8.dp)
                .zIndex(1f)
        ) {
            // Hamburger
            Image(
                painter = painterResource(R.drawable.density_medium),
                contentDescription = null,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .clickable {
                        fsViewModel.isMenuOpen_BoolSwitch()
                    }
            )

            // Choice
            AnimatedVisibility(
                visible = fsUIState.isMenuOpen,
            ) {
                Column (
                    horizontalAlignment = AbsoluteAlignment.Right
                ){
                    Spacer(modifier = Modifier.height(4.dp))

                    // Original
                    Button(
                        onClick = {
                            fsViewModel.changeSpinner(0)
                            fsViewModel.updateSettingTrue()
                            fsViewModel.updateFSData(token)

                            fsViewModel.isMenuOpen_BoolSwitch()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5E4890),
                        )
                    ) {
                        Text(
                            text = "Fidget Spinner",
                            fontFamily = FontFamily(Font(R.font.jua)),
                            fontSize = 16.sp
                        )
                    }
                    // Pinwheel
                    Button(
                        onClick = {
                            fsViewModel.changeSpinner(1)
                            fsViewModel.updateSettingTrue()
                            fsViewModel.updateFSData(token)

                            fsViewModel.isMenuOpen_BoolSwitch()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5E4890),
                        )
                    ) {
                        Text(
                            text = "Pinwheel",
                            fontFamily = FontFamily(Font(R.font.jua)),
                            fontSize = 16.sp
                        )
                    }
                    // Compass
                    Button(
                        onClick = {
                            fsViewModel.changeSpinner(2)
                            fsViewModel.updateSettingTrue()
                            fsViewModel.updateFSData(token)

                            fsViewModel.isMenuOpen_BoolSwitch()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5E4890),
                        )
                    ) {
                        Text(
                            text = "Compass",
                            fontFamily = FontFamily(Font(R.font.jua)),
                            fontSize = 16.sp
                        )
                    }
                    // Pencil
                    Button(
                        onClick = {
                            fsViewModel.changeSpinner(3)
                            fsViewModel.updateSettingTrue()
                            fsViewModel.updateFSData(token)

                            fsViewModel.isMenuOpen_BoolSwitch()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5E4890),
                        )
                    ) {
                        Text(
                            text = "Pencil",
                            fontFamily = FontFamily(Font(R.font.jua)),
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))

                    // Music player
                    Column(
                        modifier = Modifier
                            .background(
                                Color(0xFF5E4890),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "🎵",
                            color = Color(0xFFD7C4EC),
                            fontSize = 24.sp,
                            modifier = Modifier.background(Color(0xFFD7C4EC), shape = CircleShape)
                                .padding(8.dp)
                        )

                        // Music name
                        Text(
                            text = "Playing - ${fsViewModel.musicAndName.name}",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.jua)),
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Skip and pause button
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            //play pause button
                            Text(
                                text = if (fsViewModel.boolIsPlaying) "🔇" else "🔊",
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.background(
                                    color = Color(0xFFB39DDB), // Lighter purple background
                                    shape = RoundedCornerShape(20.dp)
                                )
                                    .padding(horizontal = 28.dp, vertical = 2.dp)
                                    .clickable { fsViewModel.onToggleSong() }
                            )
                            Spacer(modifier = Modifier.width(12.dp))

                            // Skip button
                            Text(
                                text = "⏭",
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.background(
                                    color = Color(0xFFB39DDB), // Lighter purple background
                                    shape = RoundedCornerShape(20.dp)
                                )
                                    .padding(horizontal = 28.dp, vertical = 2.dp)
                                    .clickable { fsViewModel.skipSong(context) }
                            )
                        }

                    }
                }

            }

        }


        // Spins score display
        Text(
            text = "Spins: ${fsViewModel.score}",
            color = Color.White,
            fontSize = 40.sp,
            fontFamily = FontFamily(Font(R.font.jua)),
            modifier = Modifier
                .offset(y = 40.dp)
                .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(20.dp))
                .padding(12.dp)
                .align(Alignment.TopCenter)
        )

        // Back button
        Image(
            painter = painterResource(R.drawable.back_button),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 32.dp)
                .size(52.dp)
                .align(Alignment.TopStart)
                .clip(CircleShape)
                .clickable {
                    fsViewModel.updateFSData(token)
                    fsViewModel.stopSong()

                    navController.navigate(PagesEnum.Home.name) {
                        popUpTo(PagesEnum.Home.name) {
                            inclusive = true
                        }
                    }
                }
        )

        // Rotating Box
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .graphicsLayer(rotationZ = rotation.value % 360)
                .transformable(stateZ)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            fsViewModel.updateFSData(token)

                            coroutineScope.launch {
                                fsViewModel.resetPrDecayTrack()

                                rotation.animateDecay(
                                    initialVelocity = fsViewModel.velocity + 50f, // Spin speed
                                    animationSpec = exponentialDecay(frictionMultiplier = 0.1f)
                                ) {
                                    fsViewModel.trackSpinDuringDecay()
                                }
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            coroutineScope.launch { rotation.stop() }

                            fsViewModel.updateFSData(token)
                        },
                        onDragEnd = {
                            coroutineScope.launch {
                                fsViewModel.resetPrDecayTrack()

                                rotation.animateDecay(
                                    initialVelocity = fsViewModel.velocity,
                                    animationSpec = exponentialDecay(frictionMultiplier = 0.08f)
                                ) {
                                    fsViewModel.trackSpinDuringDecay()
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()

                            // Calculate drag angle
                            val distance =  (dragAmount.x - dragAmount.y) * 3 // x - y means it can rotated both clockwise and vice versa
                            fsViewModel.updateVelocityAsDistance(distance)

                            coroutineScope.launch {
                                fsViewModel.rotationSnapTo_Update(distance)
                            }
                        }
                    )
                }
                .size(320.dp)
        ) {
            Image(
                painter = painterResource(fsViewModel.imageResource),
                contentDescription = null,
                modifier = Modifier.size(360.dp)
            )
        }
    }
}