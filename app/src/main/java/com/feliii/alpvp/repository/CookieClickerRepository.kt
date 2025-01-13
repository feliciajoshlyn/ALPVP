package com.feliii.alpvp.repository

import com.feliii.alpvp.model.GetCookieClickerResponse
import com.feliii.alpvp.model.UpdateCookieClickerRequest
import com.feliii.alpvp.model.GeneralResponseModel
import com.feliii.alpvp.service.CookieClickerAPIService
import retrofit2.Call

interface CookieClickerRepository {
    fun getCookieClickerData(token: String): Call<GetCookieClickerResponse>
    suspend fun updateCookieClickerData(token: String, request: UpdateCookieClickerRequest): Call<GeneralResponseModel>
}

class NetworkCookieClickerRepository(
    private val apiService: CookieClickerAPIService
) : CookieClickerRepository {
    override fun getCookieClickerData(token: String): Call<GetCookieClickerResponse> {
        return apiService.getCookieClickerData(token)
    }

    override suspend fun updateCookieClickerData(token: String, request: UpdateCookieClickerRequest): Call<GeneralResponseModel> {
        return apiService.updateCookieClickerData(token, request)
    }
}
