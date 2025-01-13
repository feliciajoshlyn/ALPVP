package com.feliii.alpvp.uiStates

import com.feliii.alpvp.model.CookieClickerModel

sealed interface CookieClickerDataStatusUIState {
    data class Success(val data: CookieClickerModel) : CookieClickerDataStatusUIState
    object Loading : CookieClickerDataStatusUIState
    object Start : CookieClickerDataStatusUIState
    data class Failed(val errorMessage: String) : CookieClickerDataStatusUIState
}
