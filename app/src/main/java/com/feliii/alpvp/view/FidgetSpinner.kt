package com.feliii.alpvp.view

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
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
    val stateZ = rememberTransformableState { _, _, rotationChange ->
        coroutineScope.launch { rotation.snapTo(rotation.value + rotationChange) }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
            .paint(
                painter = painterResource(R.drawable.wood_fsbackground),
                alignment = Alignment.Center,
                contentScale = ContentScale.FillBounds
            )
    ) {
        Image(
            painter = painterResource(R.drawable.conpass),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .offset(x = 10.dp) //conpass x = 10, pinwheel y = 150
        )



        // Back button
        Image(
            painter = painterResource(R.drawable.back_button),
            contentDescription = null,
            modifier = Modifier
                .size(52.dp)
                .align(Alignment.TopStart)
        )

        // Rotating Box with Image as Background using .paint()
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .graphicsLayer(rotationZ = rotation.value)
                .transformable(stateZ)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            coroutineScope.launch {
                                // Apply a small rotation increment
                                rotation.animateTo(rotation.value)

                                // Decay after click
                                rotation.animateDecay(
                                    initialVelocity = 500f, // Initial spin speed
                                    animationSpec = exponentialDecay(frictionMultiplier = 0.1f)
                                )
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
                                rotation.animateDecay(
                                    initialVelocity = 150f, // Adjust drag spin speed
                                    animationSpec = exponentialDecay(frictionMultiplier = 0.05f)
                                )
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            val distance = hypot(dragAmount.x, dragAmount.y)
                            coroutineScope.launch {
                                rotation.snapTo(rotation.value + distance * 0.15f)
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
