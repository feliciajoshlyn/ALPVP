package com.feliii.alpvp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.feliii.alpvp.RelaxGameApplication
import com.feliii.alpvp.enums.PagesEnum
import com.feliii.alpvp.model.ErrorModel
import com.feliii.alpvp.model.GeneralResponseModel
import com.feliii.alpvp.repository.UserRepository
import com.feliii.alpvp.uiStates.StringDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel (
    val userRepository: UserRepository
): ViewModel(){

    val username: StateFlow<String> = userRepository.currentUsername.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    var logoutStatus: StringDataStatusUIState by mutableStateOf(StringDataStatusUIState.Start)
        private set

    fun logoutUser(token: String, navController: NavHostController) {
        viewModelScope.launch() {
            logoutStatus = StringDataStatusUIState.Loading

            Log.d("logout-token", "LOGOUT TOKEN: ${token}")

            try {
                val call = userRepository.logout(token)

                call.enqueue(object: Callback<GeneralResponseModel>{
                    override fun onResponse(
                        call: Call<GeneralResponseModel>,
                        res: Response<GeneralResponseModel>
                    ) {
                        if(res.isSuccessful){
                            logoutStatus = StringDataStatusUIState.Success(data = res.body()!!.data)

                            saveUsernameToken("Unknown", "Unknown")

                            navController.navigate(PagesEnum.Login.name){
                                popUpTo(PagesEnum.Home.name){
                                    inclusive = true
                                }
                            }
                        }else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            logoutStatus = StringDataStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(call: Call<GeneralResponseModel?>, t: Throwable) {
                        logoutStatus = StringDataStatusUIState.Failed(t.localizedMessage)
                        Log.d("logout-failure", t.localizedMessage)
                    }
                })
            }catch(error: IOException){
                logoutStatus = StringDataStatusUIState.Failed(error.localizedMessage)
                Log.d("logout-error", error.localizedMessage)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as RelaxGameApplication)
                val userRepository = application.container.userRepository
                ProfileViewModel(
                    userRepository
                )
            }
        }
    }

    fun saveUsernameToken(username: String, token: String) {
        viewModelScope.launch {
            userRepository.saveUsername(username)
            userRepository.saveUserToken(token)
        }
    }

    fun clearLogoutErrorMessage() {
        logoutStatus = StringDataStatusUIState.Start
    }

    fun clearLogoutSuccessMessage() {
        logoutStatus = StringDataStatusUIState.Start
    }
}