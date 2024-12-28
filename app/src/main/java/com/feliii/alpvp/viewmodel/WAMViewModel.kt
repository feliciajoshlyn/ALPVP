package com.feliii.alpvp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.feliii.alpvp.enums.PagesEnum
import com.feliii.alpvp.model.ErrorModel
import com.feliii.alpvp.model.GetWAMResponse
import com.feliii.alpvp.repository.WAMRepository
import com.feliii.alpvp.uiStates.WAMDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class WAMViewModel (
    private val wamRepository: WAMRepository
): ViewModel() {
    var dataStatus: WAMDataStatusUIState by mutableStateOf(WAMDataStatusUIState.Start)
        private set

    fun getWAMData(token: String, navController: NavHostController, isUpdating: Boolean) {
        viewModelScope.launch {
            dataStatus = WAMDataStatusUIState.Loading

            try{
                val call = wamRepository.getWAMData(token)

                call.enqueue(object: Callback<GetWAMResponse> {
                    override fun onResponse(
                        call: Call<GetWAMResponse>,
                        res: Response<GetWAMResponse>
                    ) {
                        if(res.isSuccessful) {
                            dataStatus = WAMDataStatusUIState.Success(res.body()!!.data)

                            Log.d("get-wam-data", "GET WAM: ${res.body()}")
                        }else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )

                            dataStatus = WAMDataStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(call: Call<GetWAMResponse?>, t: Throwable) {
                        dataStatus = WAMDataStatusUIState.Failed(t.localizedMessage)
                    }
                })
            }catch(error: IOException) {
                dataStatus = WAMDataStatusUIState.Failed(error.localizedMessage)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as WAMApplication)
                val wamRepository = application.container.wamRepository
                WAMViewModel(wamRepository)
            }
        }
    }
}