package com.feliii.alpvp.repository

import com.feliii.alpvp.model.FidgetSpinnerScoreUpdate
import com.feliii.alpvp.model.FidgetSpinnerUpdateRequest
import com.feliii.alpvp.model.GeneralResponseModel
import com.feliii.alpvp.model.GetFSResponse
import com.feliii.alpvp.service.FSAPIService
import retrofit2.Call

interface FSRepository {
    fun getFSData(token: String): Call<GetFSResponse>

    suspend fun updateFSSettingData(token: String, spinner_chosen: Int, music_chosen: Int): Call<GeneralResponseModel>
    suspend fun updateFSScoreData(token: String, spins_score: Int): Call<GeneralResponseModel>
}

class NetworkFSSettingRepository(
    private val fsAPIService: FSAPIService
) : FSRepository {
    override fun getFSData(token: String): Call<GetFSResponse> {
        return fsAPIService.getFSData(token)
    }

    override suspend fun updateFSSettingData(
        token: String,
        spinner_chosen: Int,
        music_chosen: Int
    ): Call<GeneralResponseModel> {
        return fsAPIService.updateFSSetting(
            token,
            FidgetSpinnerUpdateRequest(spinner_chosen, music_chosen)
        )
    }

    override suspend fun updateFSScoreData(
        token: String,
        spins_score: Int
    ): Call<GeneralResponseModel> {
        return fsAPIService.updateFSScore(token, FidgetSpinnerScoreUpdate(spins_score))
    }
}
