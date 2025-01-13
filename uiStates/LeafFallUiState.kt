package com.feliii.alpvp.uiStates

import com.feliii.alpvp.model.BonusItem

data class LeafFallUiState(
    val score: Int = 0,
    val highScore: Int = 0,
    val fallingLeaves: List<String> = emptyList(),
    val bonusItems: List<BonusItem> = emptyList(),
    val isGameOver: Boolean = false,
    val timer: Int = 0,
    val failedAttempts: Int = 0, // Jumlah kegagalan yang terjadi
    val currentMusic: String = "game_music", // Musik yang sedang diputar
    val gameMode: String = "Classic" // Mode permainan yang dipilih: Classic, Endless, Challenge
)
