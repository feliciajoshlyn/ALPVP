package com.feliii.alpvp.service

import com.feliii.alpvp.model.GeneralResponseModel
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Header

interface UserAPIService {
    @DELETE("api/logout")
    //header utk minta token
    //kirim token  untuk diproses lewat header
    fun logout(@Header("X-API-TOKEN") token: String): Call<GeneralResponseModel>
}