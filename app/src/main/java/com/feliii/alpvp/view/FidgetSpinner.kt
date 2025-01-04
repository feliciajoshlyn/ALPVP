package com.feliii.alpvp.view

import android.annotation.SuppressLint
import android.icu.number.Scale
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.feliii.alpvp.R
import kotlinx.coroutines.launch
import kotlin.math.hypot

@Composable
fun FidgetSpinner() {
    val coroutineScope = rememberCoroutineScope()
    val rotation = remember { Animatable(0f) }
    val score = remember { mutableStateOf(0) }
    val previousRotation = remember { mutableStateOf(0f) } // Tracks previous rotation value

    val stateZ = rememberTransformableState { _, _, rotationChange ->
        coroutineScope.launch {
            rotation.snapTo(rotation.value + rotationChange)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(R.drawable.wood_fsbackground),
                alignment = Alignment.Center,
                contentScale = ContentScale.FillBounds
            )
    ) {
        // Background
        Image(
            painter = painterResource(R.drawable.conpass),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .offset(x = 10.dp)
        )

        // Display score
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .background(Color.Black, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = "Score: ${score.value}",
                color = Color.White
            )
        }

        // Back button
        Image(
            painter = painterResource(R.drawable.back_button),
            contentDescription = null,
            modifier = Modifier
                .size(52.dp)
                .align(Alignment.TopStart)
        )

        // Rotating Box with Image as Background
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .graphicsLayer(rotationZ = rotation.value % 360) // Keep within 0–360 for display
                .transformable(stateZ)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            coroutineScope.launch {
                                // Reset previous rotation for decay tracking
                                previousRotation.value = rotation.value

                                // Decay animation
                                rotation.animateDecay(
                                    initialVelocity = 500f, // Adjust spin speed
                                    animationSpec = exponentialDecay(frictionMultiplier = 0.1f)
                                ) {
                                    // Track spins during decay (clockwise and counterclockwise)
                                    val currentRotation = rotation.value
                                    val fullRotations = (currentRotation / 360).toInt() - (previousRotation.value / 360).toInt()
                                    score.value += fullRotations // Increment or decrement score based on direction
                                    previousRotation.value = currentRotation
                                }
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            coroutineScope.launch { rotation.stop() }
                        },
                        onDragEnd = {
                            coroutineScope.launch {
                                // Reset previous rotation for decay tracking
                                previousRotation.value = rotation.value

                                // Decay animation
                                rotation.animateDecay(
                                    initialVelocity = 400f,
                                    animationSpec = exponentialDecay(frictionMultiplier = 0.05f)
                                ) {
                                    // Track spins during decay (clockwise and counterclockwise)
                                    val currentRotation = rotation.value
                                    val fullRotations = (currentRotation / 360).toInt() - (previousRotation.value / 360).toInt()
                                    score.value += fullRotations // Increment or decrement score based on direction
                                    previousRotation.value = currentRotation
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()

                            // Calculate drag angle based on horizontal and vertical deltas
                            val rotationDelta = dragAmount.x - dragAmount.y
                            coroutineScope.launch {
                                rotation.snapTo(rotation.value + rotationDelta * 0.2f) // Adjust sensitivity for smoothness
                            }
                        }
                    )
                }
                .size(320.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.conpassarrow),
                contentDescription = null,
                modifier = Modifier.size(360.dp)
            )
        }
    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainMenuPreview() {
    FidgetSpinner()
}
