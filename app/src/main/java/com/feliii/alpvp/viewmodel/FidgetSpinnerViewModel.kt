package com.feliii.alpvp.viewmodel

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.feliii.alpvp.uiStates.FSDataStatusUIState
import com.feliii.alpvp.uiStates.StringDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class FidgetSpinnerViewModel(
    private val fsRepository: FSRepository
) : ViewModel() {

    var fsDataStatus: FSDataStatusUIState by mutableStateOf(FSDataStatusUIState.Start)

    var spinner_chosen by mutableStateOf(0)
        private set

    var music_chosen by mutableStateOf(0)
        private set

    var isUpdateSetting by mutableStateOf(false)

    // Frontend
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
    // Frontend



    // Backend
    var submissionStatus: StringDataStatusUIState by mutableStateOf(StringDataStatusUIState.Start)
        private set

    fun getFSData(token: String, navController: NavHostController) {
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
                            fsDataStatus = FSDataStatusUIState.Success(res.body()!!.data)

                            Log.d("get-fs-data", "GET FS: ${res.body()}")

//                            navController.navigate(PagesEnum.WhackAMoleMenu.name) {
//                            }
                        }else {
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


    fun updateFSData(token: String, getFS: () -> Unit) {
        viewModelScope.launch {
            submissionStatus = StringDataStatusUIState.Loading

            try {
                var call = fsRepository.updateFSScoreData(token, score)

                if (isUpdateSetting){
                    call = fsRepository.updateFSSettingData(token, spinner_chosen, music_chosen)
                }

                call.enqueue(object : Callback<GeneralResponseModel> {
                    override fun onResponse(
                        call: Call<GeneralResponseModel>,
                        res: Response<GeneralResponseModel>
                    ) {
                        if (res.isSuccessful) {
                            submissionStatus = StringDataStatusUIState.Success(res.body()!!.data)

                            getFS()

                        } else {
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
