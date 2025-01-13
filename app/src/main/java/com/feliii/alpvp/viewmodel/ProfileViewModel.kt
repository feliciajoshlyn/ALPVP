package com.feliii.alpvp.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
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
import com.feliii.alpvp.repository.UserRepository
import com.feliii.alpvp.uiStates.StringDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(
    val userRepository: UserRepository
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Fetch the application instance
                val application = (this[APPLICATION_KEY] as RelaxGameApplication)
                val userRepository = application.container.userRepository
                ProfileViewModel(userRepository)
            }
        }
    }

    val username: StateFlow<String> = userRepository.currentUsername.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    var logoutStatus: StringDataStatusUIState by mutableStateOf(StringDataStatusUIState.Start)
        private set

    private val _selectedSong = MutableStateFlow<MainMenuBackgroundSong?>(null) // Default is null
    val selectedSong: StateFlow<MainMenuBackgroundSong?> get() = _selectedSong

    private var mediaPlayer: MediaPlayer? = null

    private val songList = listOf(
        MainMenuBackgroundSong("Cloud Drift", R.raw.cloudrift),
        MainMenuBackgroundSong("Dreamscape", R.raw.dreamscape),
        MainMenuBackgroundSong("Infinite Horizon", R.raw.infinitehorizon),
        MainMenuBackgroundSong("Starlit Menu", R.raw.starlit)
    )

    fun getSongList(): List<MainMenuBackgroundSong> = songList

    fun setSelectedSong(songName: String, context: Context) {
        val song = songList.find { it.name == songName }
        if (song != null) {
            _selectedSong.value = song
            playMusic(context)
        }
    }

    fun playMusic(context: Context) {
        stopMusic()
        _selectedSong.value?.let { song ->
            mediaPlayer = MediaPlayer.create(context, song.resourceId).apply {
                isLooping = true
                start()
            }
        }
    }

    fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun logoutUser(token: String, navController: NavHostController) {
        stopMusic() // Stop music when logging out
        viewModelScope.launch {
            logoutStatus = StringDataStatusUIState.Loading
            try {
                val call = userRepository.logout(token)
                call.enqueue(object : Callback<GeneralResponseModel> {
                    override fun onResponse(
                        call: Call<GeneralResponseModel>,
                        response: Response<GeneralResponseModel>
                    ) {
                        if (response.isSuccessful) {
                            logoutStatus = StringDataStatusUIState.Success(data = response.body()!!.data)
                            saveUsernameToken("Unknown", "Unknown")
                            navController.navigate(PagesEnum.Login.name) {
                                popUpTo(PagesEnum.Home.name) { inclusive = true }
                            }
                        } else {
                            val errorMessage = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            logoutStatus = StringDataStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(call: Call<GeneralResponseModel>, t: Throwable) {
                        logoutStatus = StringDataStatusUIState.Failed(t.localizedMessage)
                    }
                })
            } catch (e: IOException) {
                logoutStatus = StringDataStatusUIState.Failed(e.localizedMessage)
            }
        }
    }

    fun saveUsernameToken(username: String, token: String) {
        viewModelScope.launch {
            userRepository.saveUsername(username)
            userRepository.saveUserToken(token)
        }
    }

    fun clearLogoutErrorMessage() {
        logoutStatus = StringDataStatusUIState.Start
    }

    fun clearLogoutSuccessMessage() {
        logoutStatus = StringDataStatusUIState.Start
    }

    override fun onCleared() {
        super.onCleared()
        stopMusic()
    }
}



// Data class for songs
data class MainMenuBackgroundSong(
    val name: String,
    val resourceId: Int
)
