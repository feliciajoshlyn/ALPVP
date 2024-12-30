package com.feliii.alpvp.service

import com.feliii.alpvp.GeneralResponseModel
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface UserAPIService {
    @POST("api/logout")
    //header utk minta token
    //kirim token  untuk diproses lewat header
    fun logout(@Header("X-API-TOKEN") token: String): Call<GeneralResponseModel>
}