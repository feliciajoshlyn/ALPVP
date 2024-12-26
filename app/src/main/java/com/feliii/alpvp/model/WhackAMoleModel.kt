package com.feliii.alpvp.model

data class GetAllWAMResponses(
    val data: List<WhackAMoleModel>
)

data class GetWAMResponse(
    val data: WhackAMoleModel
)

data class WhackAMoleModel(
    val id: Int = 0,
    val mole_chosen: String = "",
    val song_chosen: String = "",
    val timed_highscore: Int = 0,
    val endless_highscore: Int = 0,
    val intense_highscore: Int = 0,
)

data class WhackAMoleRequest(
    val mole_chosen: String = "",
    val song_chosen: String = "",
    val timed_highscore: Int = 0,
    val endless_highscore: Int = 0,
)