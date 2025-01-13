package com.feliii.alpvp.model

import com.feliii.alpvp.enums.LeafImage
import com.feliii.alpvp.enums.BonusItemType

data class LeafFall(
    val id: Int,
    val userId: Int,
    val gameMode: String,
    val score: Int,
    val highScore: Int,
    val leafItems: List<LeafItem>,
    val bonusItems: List<BonusItem>,
    val musicPath: String
)

data class LeafItem(
    val leafType: LeafImage,
    val imagePath: String
)

data class BonusItem(
    val itemType: BonusItemType,
    val imagePath: String
)
