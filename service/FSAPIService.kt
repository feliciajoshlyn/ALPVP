package com.feliii.alpvp.service

import com.feliii.alpvp.model.FidgetSpinnerScoreUpdate
import com.feliii.alpvp.model.FidgetSpinnerUpdateRequest
import com.feliii.alpvp.model.GeneralResponseModel
import com.feliii.alpvp.model.GetFSResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface FSAPIService {
    // perlu token to authenticate user -Felicia

    @GET("api/fs")
    fun getFSData(@Header("X-API-TOKEN") token: String): Call<GetFSResponse>

    @PUT("api/fs/setting")
    fun updateFSSetting(@Header("X-API-TOKEN") token: String, @Body fsModel: FidgetSpinnerUpdateRequest): Call<GeneralResponseModel>

    @PUT("api/fs/score")
    fun updateFSScore(@Header("X-API-TOKEN") token: String, @Body fsModel: FidgetSpinnerScoreUpdate): Call<GeneralResponseModel>
}