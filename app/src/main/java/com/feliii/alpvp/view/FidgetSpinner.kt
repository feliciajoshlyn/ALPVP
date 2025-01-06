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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
    var isMenuOpen by remember { mutableStateOf(false) }

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
                    .offset(x = 10.dp)
            )
        }

                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(if (isMenuOpen) Color.White else Color.Transparent, shape = RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    if (!isMenuOpen){
                        Image(
                            painter = painterResource(R.drawable.density_medium),
                            contentDescription = null,
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .clickable {
                                    isMenuOpen = true
                                }
                        )
                    }
                    else {
                        Button(
                            onClick = {
                                fsViewModel.changeSpinner(0)
                                fsViewModel.updateSettingTrue()
                                fsViewModel.updateFSData(token)

                                isMenuOpen = false
                            }
                        ) {
                            Text("Fidget Spinner")
                        }
                        Button(
                            onClick = {
                                fsViewModel.changeSpinner(1)
                                fsViewModel.updateSettingTrue()
                                fsViewModel.updateFSData(token)

                                isMenuOpen = false
                            }
                        ) {
                            Text("Pinwheel")
                        }
                        Button(
                            onClick = {
                                fsViewModel.changeSpinner(2)
                                fsViewModel.updateSettingTrue()
                                fsViewModel.updateFSData(token)

                                isMenuOpen = false
                            }
                        ) {
                            Text("Compass")
                        }
                    }

                }



        // Spins score display
        Text(
            text = "Score: ${fsViewModel.score}",
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