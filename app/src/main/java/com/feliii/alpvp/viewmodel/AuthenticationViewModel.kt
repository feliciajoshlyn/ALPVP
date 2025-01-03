package com.feliii.alpvp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.feliii.alpvp.R
import com.feliii.alpvp.RelaxGameApplication
import com.feliii.alpvp.enums.PagesEnum
import com.feliii.alpvp.model.ErrorModel
import com.feliii.alpvp.model.UserResponse
import com.feliii.alpvp.repository.AuthenticationRepository
import com.feliii.alpvp.repository.UserRepository
import com.feliii.alpvp.uiStates.AuthenticationStatusUIState
import com.feliii.alpvp.uiStates.AuthenticationUIState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Callback
import okio.IOException
import retrofit2.Call
import retrofit2.Response

class AuthenticationViewModel (
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository
): ViewModel(){
    private val _authenticationUIState = MutableStateFlow(AuthenticationUIState())

    val authenticationUIState: StateFlow<AuthenticationUIState>
        get() {
            return _authenticationUIState.asStateFlow()
        }

    var dataStatus: AuthenticationStatusUIState by mutableStateOf(AuthenticationStatusUIState.Start)
        private set

    var usernameInput by mutableStateOf("")
        private set

    var passwordInput by mutableStateOf("")
        private set

    var confirmPasswordInput by mutableStateOf("")
        private set

    fun changeUsernameInput(usernameInput: String){
        this.usernameInput = usernameInput
    }

    fun changePasswordInput(passwordInput: String){
        this.passwordInput = passwordInput
    }

    fun changeConfirmPasswordInput(confirmPasswordInput: String){
        this.confirmPasswordInput = confirmPasswordInput
    }

    fun changePasswordVisibility() {
        _authenticationUIState.update { currentState ->
            if (currentState.showPassword) {
                currentState.copy(
                    showPassword = false,
                    passwordVisibility = PasswordVisualTransformation(),
                )
            } else {
                currentState.copy(
                    showPassword = true,
                    passwordVisibility = VisualTransformation.None,
                )
            }
        }
    }

    fun changeConfirmPasswordVisibility() {
        _authenticationUIState.update { currentState ->
            if (currentState.showConfirmPassword) {
                currentState.copy(
                    showConfirmPassword = false,
                    passwordVisibility = PasswordVisualTransformation(),
                )
            } else {
                currentState.copy(
                    showConfirmPassword = true,
                    passwordVisibility = VisualTransformation.None,
                )
            }
        }
    }

    fun checkLoginForm() {
        if (usernameInput.isNotEmpty() && passwordInput.isNotEmpty()) {
            _authenticationUIState.update { currentState ->
                currentState.copy(
                    buttonEnabled = true
                )
            }
        }
        else {
            _authenticationUIState.update { currentState ->
                currentState.copy(
                    buttonEnabled = false
                )
            }
        }
    }

    fun checkRegisterForm() {
        if (usernameInput.isNotEmpty() && passwordInput.isNotEmpty() && confirmPasswordInput.isNotEmpty()) {
            _authenticationUIState.update { currentState ->
                currentState.copy(
                    buttonEnabled = true
                )
            }
        }
        else {
            _authenticationUIState.update { currentState ->
                currentState.copy(
                    buttonEnabled = false
                )
            }
        }
    }

    fun registerUser(navController: NavHostController) {
        viewModelScope.launch {
            dataStatus = AuthenticationStatusUIState.Loading

            try {
                if (confirmPasswordInput != passwordInput){
                    Log.d("RegisterUser", "Password and confirm password does not match.")
                    dataStatus =  AuthenticationStatusUIState.Failed("Password and confirm password does not match.")
                }
                else {
                    val call = authenticationRepository.register(usernameInput, passwordInput)

                    call.enqueue(object : Callback<UserResponse> {
                        override fun onResponse(call: Call<UserResponse>, res: Response<UserResponse>) {
                            if (res.isSuccessful && res.body() != null) {
                                val userData = res.body()!!.data

                                saveUsernameToken(userData.username, userData.token ?: "")
                                dataStatus = AuthenticationStatusUIState.Success(userData)

                                resetViewModel()

                                // Navigate to Home after successful registration
                                navController.navigate(PagesEnum.Home.name) {
                                    popUpTo(PagesEnum.Login.name) {
                                        inclusive = true
                                    }
                                }
                            } else {
                                // Handle error response
                                val errorMessage = try {
                                    res.errorBody()?.charStream()?.let {
                                        Gson().fromJson(it, ErrorModel::class.java)
                                    }?.errors ?: "Username already exist"
                                } catch (e: Exception) {
                                    "Error parsing response: ${e.localizedMessage}"
                                }

                                Log.d("RegisterUser", "Registration failed: $errorMessage")
                                dataStatus = AuthenticationStatusUIState.Failed(errorMessage)
                            }
                        }

                        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                            Log.d("RegisterUser", "Network failure: ${t.localizedMessage}")
                            dataStatus = AuthenticationStatusUIState.Failed("Network error: ${t.localizedMessage}")
                        }
                    })
                }
            } catch (error: IOException) {
                dataStatus = AuthenticationStatusUIState.Failed("IOException: ${error.localizedMessage}")
                Log.d("RegisterUser", "IOException during registration: ${error.localizedMessage}")
            }
        }
    }


    fun loginUser(
        navController: NavHostController
    ) {
        viewModelScope.launch {
            dataStatus = AuthenticationStatusUIState.Loading
            try {
                val call = authenticationRepository.login(usernameInput, passwordInput)
                call.enqueue(object : Callback<UserResponse> {
                    override fun onResponse(
                        call: Call<UserResponse>,
                        res: Response<UserResponse>
                    ) {
                        if (res.isSuccessful) {
                            saveUsernameToken(res.body()!!.data.username, res.body()!!.data.token!!)

                            dataStatus = AuthenticationStatusUIState.Success(res.body()!!.data)

                            resetViewModel()

                            navController.navigate(PagesEnum.Home.name) {
                                popUpTo(PagesEnum.Login.name) {
                                    inclusive = true
                                }
                            }
                        } else {
                            try {
                                val errorMessage = Gson().fromJson(
                                    res.errorBody()!!.charStream(),
                                    ErrorModel::class.java
                                )

                                Log.d("error-data", "Invalid username or password")
                                dataStatus = AuthenticationStatusUIState.Failed("Invalid username or password")

                            } catch (e: Exception) {
                                // In case of an error in parsing the error response, provide a fallback message
                                Log.d("error-data", "Error parsing the error response: ${e.localizedMessage}")
                                dataStatus = AuthenticationStatusUIState.Failed("An unknown error occurred")
                            }
                        }
                    }

                    override fun onFailure(call: Call<UserResponse?>, t: Throwable) {
                        dataStatus = AuthenticationStatusUIState.Failed(t.localizedMessage)
                    }
                })
            }catch(error: IOException){
                dataStatus = AuthenticationStatusUIState.Failed(error.localizedMessage)
                Log.d("login-error", "LOGIN ERROR: ${error.localizedMessage}")
            }
        }
    }

    fun saveUsernameToken(username: String, token: String){
        viewModelScope.launch {
            userRepository.saveUsername(username)
            userRepository.saveUserToken(token)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as RelaxGameApplication)
                val authenticationRepository = application.container.authenticationRepository
                val userRepository = application.container.userRepository
                AuthenticationViewModel(authenticationRepository, userRepository)
            }
        }
    }

    fun resetViewModel() {
        changeUsernameInput("")
        changePasswordInput("")
        changeConfirmPasswordInput("")
        _authenticationUIState.update { currentState ->
            currentState.copy(
                showPassword = false,
                showConfirmPassword = false,
                passwordVisibility = PasswordVisualTransformation(),
                buttonEnabled = false
            )
        }
        dataStatus = AuthenticationStatusUIState.Start
    }

    fun clearErrorMessage() {
        dataStatus = AuthenticationStatusUIState.Start
    }
}