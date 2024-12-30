package com.feliii.alpvp.repository

import com.feliii.alpvp.model.GeneralResponseModel
import com.feliii.alpvp.model.GetWAMResponse
import com.feliii.alpvp.model.WhackAMoleRequest
import com.feliii.alpvp.service.WAMAPIService
import retrofit2.Call

interface WAMRepository {
    fun getWAMData(token: String): Call<GetWAMResponse>

    suspend fun updateWAMData(token: String, mole_chosen: String, song_chosen: String, timed_highscore: Int, endless_highscore: Int, intense_highscore: Int): Call<GeneralResponseModel>
}

class NetworkWAMRepository(
    private val wamAPIService: WAMAPIService
): WAMRepository {
    override fun getWAMData(token: String): Call<GetWAMResponse> {
        return wamAPIService.getWAMData(token)
    }

    override suspend fun updateWAMData(
        token: String,
        mole_chosen: String,
        song_chosen: String,
        timed_highscore: Int,
        endless_highscore: Int,
        intense_highscore: Int
    ): Call<GeneralResponseModel> {
        return wamAPIService.updateWAMData(
            token,
            WhackAMoleRequest(mole_chosen, song_chosen, timed_highscore, endless_highscore, intense_highscore)
        )
    }
}