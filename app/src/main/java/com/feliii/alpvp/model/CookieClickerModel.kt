package com.feliii.alpvp.model

data class GetCookieClickerResponse(
    val data: CookieClickerModel
)

data class CookieClickerModel(
    val id: Int = 0,
    val total_cookies: Int = 0,
    val upgrade_power: Int = 1
)

data class UpdateCookieClickerRequest(
    val total_cookies: Int,
    val upgrade_power: Int
)
