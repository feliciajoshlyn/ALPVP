package com.feliii.alpvp.uiStates

import com.feliii.alpvp.model.UserModel

//to know the state of backend
sealed interface AuthenticationStatusUIState {
    data class Success(val userModelData: UserModel): AuthenticationStatusUIState
    object Loading: AuthenticationStatusUIState
    object Start: AuthenticationStatusUIState
    data class Failed(val errorMessage: String) : AuthenticationStatusUIState
}