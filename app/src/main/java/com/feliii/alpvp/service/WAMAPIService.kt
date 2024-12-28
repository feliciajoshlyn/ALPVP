package com.feliii.alpvp.service

import com.feliii.alpvp.model.GeneralResponseModel
import com.feliii.alpvp.model.GetWAMResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface WAMAPIService {
    //perlu token to authenticate user
    @GET("api/wam")
    fun getWAMData(@Header("X-API-TOKEN") token: String): Call<GetWAMResponse>

    @PUT("api/wam")
    fun updateWAMData(@Header("X-API-TOKEN") token: String): Call<GeneralResponseModel>
}