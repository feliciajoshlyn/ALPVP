package com.feliii.alpvp.viewmodel

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.feliii.alpvp.R
import com.feliii.alpvp.RelaxGameApplication
import com.feliii.alpvp.enums.PagesEnum
import com.feliii.alpvp.model.ErrorModel
import com.feliii.alpvp.model.GeneralResponseModel
import com.feliii.alpvp.model.GetFSResponse
import com.feliii.alpvp.repository.FSRepository
import com.feliii.alpvp.uiStates.AuthenticationUIState
import com.feliii.alpvp.uiStates.FSDataStatusUIState
import com.feliii.alpvp.uiStates.FidgetSpinnerUIState
import com.feliii.alpvp.uiStates.StringDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import kotlin.math.absoluteValue

class FidgetSpinnerViewModel(
    private val fsRepository: FSRepository
) : ViewModel()
{

    // UI State
    private val _fsUIState = MutableStateFlow(FidgetSpinnerUIState())

    val fsUIState: StateFlow<FidgetSpinnerUIState>
        get() {
            return _fsUIState.asStateFlow()
        }

    fun isMenuOpen_BoolSwitch(){
        _fsUIState.update { currentState ->
            if (currentState.isMenuOpen) {
                currentState.copy(
                    isMenuOpen = false,
                )
            } else {
                currentState.copy(
                    isMenuOpen = true,
                )
            }
        }
    }
    // UI State

    var fsDataStatus: FSDataStatusUIState by mutableStateOf(FSDataStatusUIState.Start)

    var spinner_chosen by mutableStateOf(0)
        private set

    fun changeSpinner(id: Int){
        spinner_chosen = id
        changeSpinnerImage()
    }
    fun changeSpinnerImage(){
        /* List:
        0 = fidget spinner
        1 = pinwheel
        2 = compass
        3 = pencil
        */

        // Spinner Image
        if (spinner_chosen == 0) {
            imageResource = R.drawable.fidgetspinner
            backItemImageResource = 0
            offsetX = 0.dp
            offsetY = 0.dp
        }
        else if (spinner_chosen == 1){
            imageResource = R.drawable.pinwheel_play
            backItemImageResource = R.drawable.pinwheel_stick
            offsetX = 0.dp
            offsetY = 150.dp
        }
        else if (spinner_chosen == 2){
            imageResource = R.drawable.conpassarrow
            backItemImageResource = R.drawable.conpass
            offsetX = 10.dp
            offsetY = 0.dp
        }
        else {
            imageResource = R.drawable.pencil_vertical
            backItemImageResource = R.drawable.papper_fs
            offsetX = 0.dp
            offsetY = 0.dp
        }

        // Background
        if (spinner_chosen == 1){
            backgroundImageResource = R.drawable.pinwheel_fsbackground
        }
        else {
            backgroundImageResource = R.drawable.wood_fsbackground
        }
    }

    var music_chosen by mutableStateOf(0)
        private set

    // this var is to check if update changing setting or not (score)
    var isUpdateSetting by mutableStateOf(false)
        private set
    fun updateSettingTrue(){
        isUpdateSetting = true
    }

    // Frontend
    var rotation = Animatable(0f)
        private set

    var velocity by mutableStateOf(0f)
        private set

    var previousRotation by mutableStateOf(0f) // For tracking rotations during decay
        private set

    var score by mutableStateOf(0)

    var backgroundImageResource = R.drawable.wood_fsbackground
    var backItemImageResource = 0
    var imageResource = R.drawable.fidgetspinner

    var offsetX = 0.dp
    var offsetY = 0.dp

    fun resetPrDecayTrack(){
        // Pr = previous rotation
        previousRotation = rotation.value
    }
    fun trackSpinDuringDecay() {
        val currentRotation = rotation.value //AbsoluteValue so it cant be negative when spinned counterclockwise
        val fullRotations = ((currentRotation / 360).toInt() - (previousRotation / 360).toInt()).absoluteValue

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
    // Frontend



    // Backend
    var submissionStatus: StringDataStatusUIState by mutableStateOf(StringDataStatusUIState.Start)
        private set

    fun getFSData(token: String) {
        viewModelScope.launch {
            fsDataStatus = FSDataStatusUIState.Loading

            try{
                val call = fsRepository.getFSData(token)

                call.enqueue(object: Callback<GetFSResponse> {
                    override fun onResponse(
                        call: Call<GetFSResponse>,
                        res: Response<GetFSResponse>
                    ) {
                        if(res.isSuccessful) {
                            val responseBody = res.body()
                            if (responseBody != null) {
                                fsDataStatus = FSDataStatusUIState.Success(responseBody.data)

                                Log.d("get-fs-data", "GET FS: $responseBody")
                                spinner_chosen = responseBody.data.spinner_chosen
                                music_chosen = responseBody.data.music_chosen
                                score = responseBody.data.spins_score

                                changeSpinnerImage()
                            }
                            else {
                                fsDataStatus = FSDataStatusUIState.Failed("Unexpected null response body")
                            }
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )

                            fsDataStatus = FSDataStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(p0: Call<GetFSResponse>, t: Throwable) {
                        fsDataStatus = FSDataStatusUIState.Failed(t.localizedMessage)
                    }
                })
            }catch(error: java.io.IOException) {
                fsDataStatus = FSDataStatusUIState.Failed(error.localizedMessage)
            }
        }
    }


    fun updateFSData(token: String) {
        viewModelScope.launch {
            submissionStatus = StringDataStatusUIState.Loading

            try {
                var call = fsRepository.updateFSScoreData(token, score)

                if (isUpdateSetting){
                    call = fsRepository.updateFSSettingData(token, spinner_chosen, music_chosen)
                    isUpdateSetting = false
                }

                call.enqueue(object : Callback<GeneralResponseModel> {
                    override fun onResponse(
                        call: Call<GeneralResponseModel>,
                        res: Response<GeneralResponseModel>
                    ) {
                        if (res.isSuccessful) {
                            submissionStatus = StringDataStatusUIState.Success(res.body()!!.data)
                        }
                        else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            submissionStatus = StringDataStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(call: Call<GeneralResponseModel?>, t: Throwable) {
                        submissionStatus = StringDataStatusUIState.Failed(t.localizedMessage)
                    }
                })
            } catch (error: IOException) {
                submissionStatus = StringDataStatusUIState.Failed(error.localizedMessage)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as RelaxGameApplication)
                val fsRepository = application.container.fsRepository
                FidgetSpinnerViewModel(fsRepository)
            }
        }
    }
    // Backend
}
