package com.feliii.alpvp.viewmodel

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.feliii.alpvp.RelaxGameApplication
import com.feliii.alpvp.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CookieClickerViewModel : ViewModel() {

    // Game State
    private val _totalCookies = MutableStateFlow(0)
    val totalCookies: StateFlow<Int> get() = _totalCookies

    private val _upgradePower = MutableStateFlow(1)
    val upgradePower: StateFlow<Int> get() = _upgradePower

    private val _currentSong = MutableStateFlow(BackgroundSong("Sugar Dream", R.raw.sugardream))
    val currentSong: StateFlow<BackgroundSong> get() = _currentSong

    private var mediaPlayer: MediaPlayer? = null

    val songList = listOf(
        BackgroundSong("Sugar Dream", R.raw.sugardream),
        BackgroundSong("Caramel Clouds", R.raw.caramelclouds),
        BackgroundSong("Choco Chill", R.raw.chocochill),
        BackgroundSong("Vanilla", R.raw.vanilla)
    )

    private val _volumeLevel = MutableStateFlow(1.0f) // Default volume: Max
    val volumeLevel: StateFlow<Float> get() = _volumeLevel

    // Handles the game logic and music playback
    fun setVolume(level: Float) {
        _volumeLevel.value = level.coerceIn(0f, 1f)
        mediaPlayer?.setVolume(_volumeLevel.value, _volumeLevel.value)
    }

    fun clickCookie() {
        _totalCookies.value += _upgradePower.value
    }

    fun upgradeClickPower(cost: Int) {
        if (_totalCookies.value >= cost) {
            _totalCookies.value -= cost
            _upgradePower.value++
        }
    }

    fun setChosenSong(songName: String, context: Context) {
        songList.find { it.name == songName }?.let { song ->
            _currentSong.value = song
            startMusic(context)
        }
    }

    fun startMusic(context: Context) {
        try {
            stopMusic()
            mediaPlayer = MediaPlayer.create(context, _currentSong.value.resourceId).apply {
                isLooping = true
                setVolume(_volumeLevel.value, _volumeLevel.value)
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopMusic()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as RelaxGameApplication)
                CookieClickerViewModel()
            }
        }
    }
}

// Data class for songs
data class BackgroundSong(
    val name: String,
    val resourceId: Int
)
