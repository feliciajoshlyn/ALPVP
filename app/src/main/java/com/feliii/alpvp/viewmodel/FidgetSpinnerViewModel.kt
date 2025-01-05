package com.feliii.alpvp.viewmodel

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feliii.alpvp.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FidgetSpinnerViewModel : ViewModel() {

    var rotation = Animatable(0f)
        private set

    var velocity by mutableStateOf(0f)
        private set

    var previousRotation by mutableStateOf(0f) // For tracking rotations during decay
        private set

    var score by mutableStateOf(0)
        private set

    var backgroundImageResource = R.drawable.wood_fsbackground
    var backItemImageResource = 0
    var imageResource = R.drawable.fidgetspinner

    fun resetPrDecayTrack(){
        // Pr = previous rotation
        previousRotation = rotation.value
    }
    fun trackSpinDuringDecay() {
        val currentRotation = rotation.value
        val fullRotations = (currentRotation / 360).toInt() - (previousRotation / 360).toInt()

        score += fullRotations
        previousRotation = currentRotation
    }

    fun updateVelocityAsDistance(distance: Float) {
        velocity = distance
    }
    suspend fun rotationSnapTo_Update(distance: Float){
        rotation.snapTo(rotation.value + distance * 0.1f)
    }

    fun updateRotation(rotationChange: Float) {
        viewModelScope.launch {
            rotation.snapTo(rotation.value + rotationChange)
        }
    }

}
