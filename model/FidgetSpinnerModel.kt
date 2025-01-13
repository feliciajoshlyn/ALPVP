package com.feliii.alpvp.model

data class FidgetSpinnerUpdateRequest (
    val spinner_chosen: Int = 0,
    val music_chosen: Int = 0
)

data class FidgetSpinnerScoreUpdate (
    val spins_score: Int = 0
)

data class GetFSResponse(
    val data: FidgetSpinnerModel
)

data class FidgetSpinnerModel(
    val id: Int = 0,
    val spinner_chosen: Int = 0,
    val music_chosen: Int = 0,
    val spins_score: Int = 0
)