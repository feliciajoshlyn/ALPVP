package com.feliii.alpvp.service

import com.feliii.alpvp.model.GeneralResponseModel
import com.feliii.alpvp.model.LeafFall
import com.feliii.alpvp.model.LeafFallRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface LeafFallAPIService {

    // Perlu token untuk autentikasi pengguna
    @GET("api/leafFall")
    fun getLeafFallData(@Header("X-API-TOKEN") token: String): Call<List<LeafFall>>

    @PUT("api/leafFall")
    fun updateLeafFallData(@Header("X-API-TOKEN") token: String, @Body leafFallRequest: LeafFallRequest): Call<GeneralResponseModel>
}
