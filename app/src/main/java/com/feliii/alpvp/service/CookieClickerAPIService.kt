package com.feliii.alpvp.service

import com.feliii.alpvp.model.GetCookieClickerResponse
import com.feliii.alpvp.model.UpdateCookieClickerRequest
import com.feliii.alpvp.model.GeneralResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface CookieClickerAPIService {
    @GET("api/cookie-clicker")
    fun getCookieClickerData(@Header("X-API-TOKEN") token: String): Call<GetCookieClickerResponse>

    @PUT("api/cookie-clicker")
    fun updateCookieClickerData(
        @Header("X-API-TOKEN") token: String,
        @Body request: UpdateCookieClickerRequest
    ): Call<GeneralResponseModel>
}
