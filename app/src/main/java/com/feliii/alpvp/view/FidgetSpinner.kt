package com.feliii.alpvp.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.feliii.alpvp.R
import com.feliii.alpvp.enums.PagesEnum
import kotlinx.coroutines.launch
import kotlin.math.hypot

@Composable
fun FidgetSpinner(
    token: String,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()

    val rotation = remember { Animatable(0f) }
    var velocity by remember { mutableStateOf(0f) } // Velocity of the spinner

    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
        // Back button
        Image(
            painter = painterResource(R.drawable.back_button),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(52.dp)
                .clickable {
                    navController.navigate(PagesEnum.Home.name){
                        popUpTo(PagesEnum.Home.name){
                            inclusive = true
                        }
                    }
                }
                .padding(2.dp)
                .align(Alignment.TopStart)
        )

        Box(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            // Interrupt ongoing decay animation if drag start
                            coroutineScope.launch {
                                rotation.stop() // Stops ongoing animation
                            }
                        },
                        onDragEnd = {
                            // Start a decay animation after stop being dragged (spinning long length slowly stopped)
                            coroutineScope.launch {
                                rotation.animateDecay(
                                    initialVelocity = velocity,
                                    animationSpec = exponentialDecay(frictionMultiplier = 0.05f)
                                )
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume() // Consume the gesture
                            // Calculate drag distance magnitude / gesture force
                            val distance = hypot(dragAmount.x, dragAmount.y)
                            velocity = distance // Update based drag speed
                            coroutineScope.launch {
                                rotation.snapTo(rotation.value + distance * 0.15f) // Update rotation
                            }
                        }
                    )
                }
        ) {

            Image(
                painter = painterResource(id = R.drawable.fidgetspinner),
                contentDescription = "Fidget Spinner",
                modifier = Modifier
                    .size(360.dp)
                    .graphicsLayer {
                        rotationZ = rotation.value % 360
                        transformOrigin = TransformOrigin(0.5f, 0.5f) // mawaru center
                }
            )
        }
    }

}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun MainMenuPreview() {
//    FidgetSpinner()
//}
