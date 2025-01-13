package com.feliii.alpvp.viewmodel

import android.content.Context
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import com.feliii.alpvp.uiStates.LeafFallUiState
import com.feliii.alpvp.enums.LeafFallGameModeEnum
import com.feliii.alpvp.R

class LeafFallViewModel : ViewModel() {

    private var _uiState = mutableStateOf(LeafFallUiState())
    val uiState: State<LeafFallUiState> get() = _uiState

    // ID gambar dan musik sesuai mode game
    private val images = mapOf(
        "leaf1" to R.drawable.leaf1,
        "leaf2" to R.drawable.leaf2,
        "leaf3" to R.drawable.leaf3,
        "bomb" to R.drawable.bomb
    )

    private val music = mapOf(
        "game_music" to R.raw.game_music,
        "endless_music" to R.raw.endless_music,
        "challenge_music" to R.raw.challenge_music
    )

    fun updateGameMode(mode: LeafFallGameModeEnum) {
        // Update state berdasarkan mode permainan yang dipilih
        _uiState.value = _uiState.value.copy(
            gameMode = mode.name,
            currentMusic = music[mode.musicKey] ?: music["game_music"]!!
        )
    }

    fun startGame(context: Context) {
        // Menginisialisasi atau mereset status permainan saat permainan dimulai
        _uiState.value = LeafFallUiState(
            score = 0,
            highScore = 0,
            fallingLeaves = generateFallingLeaves(),
            bonusItems = emptyList(),
            isGameOver = false,
            timer = 0,
            failedAttempts = 0,
            currentMusic = _uiState.value.currentMusic,
            gameMode = _uiState.value.gameMode
        )
    }

    private fun generateFallingLeaves(): List<Int> {
        // Menghasilkan daun yang jatuh secara acak
        return List(10) { images["leaf${(1..3).random()}"]!! }
    }
}
