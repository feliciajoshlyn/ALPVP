package com.feliii.alpvp.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import com.feliii.alpvp.RelaxGameApplication
import com.feliii.alpvp.enums.PagesEnum
import com.feliii.alpvp.model.ErrorModel
import com.feliii.alpvp.model.GetCalendarResponse
import com.feliii.alpvp.repository.CalendarRepository
import com.feliii.alpvp.uiStates.CalendarDetailDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.time.format.DateTimeFormatter

class CalendarDetailViewModel(
    private val calendarRepository: CalendarRepository
): ViewModel() {
    var dataStatus: CalendarDetailDataStatusUIState by mutableStateOf(CalendarDetailDataStatusUIState.Start)
        private set

    var dateChosen: String by mutableStateOf("")
        private set

    var moodChosen: List<Int> by mutableStateOf(emptyList())
        private set

    var note: String by mutableStateOf("")
        private set

    var dayOfWeek: String by mutableStateOf("")
        private set

    var monthYear: String by mutableStateOf("")
        private set

    fun getCalendarDetailData(token: String,navController: NavHostController, date: String) {
        viewModelScope.launch {
            dataStatus = CalendarDetailDataStatusUIState.Loading
            try {
                val call = calendarRepository.getEntry(token, date)

                call.enqueue(object : Callback<GetCalendarResponse> {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onResponse(
                        call: Call<GetCalendarResponse>,
                        res: Response<GetCalendarResponse>
                    ) {
                        if (res.isSuccessful) {
                            dataStatus = CalendarDetailDataStatusUIState.Success(res.body()!!.data)

                            Log.d("get-calendar-detail-data", "GET CALENDAR DETAIL: ${res.body()}")
                            note = res.body()!!.data.note
                            moodChosen = res.body()!!.data.moods
                            dateChosen = date
                            val localDate = java.time.LocalDate.parse(dateChosen)
                            dayOfWeek = localDate.format(DateTimeFormatter.ofPattern("EEEE"))
                            monthYear = localDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))

                            navController.navigate(PagesEnum.TodayMood.name)


                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            dataStatus = CalendarDetailDataStatusUIState.Failed(errorMessage.errors)
                        }
                    }
                    override fun onFailure(call: Call<GetCalendarResponse>, t: Throwable) {
                        dataStatus = CalendarDetailDataStatusUIState.Failed(t.localizedMessage)
                    }
                })
            }catch (error: IOException){
                dataStatus = CalendarDetailDataStatusUIState.Failed(error.localizedMessage)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as RelaxGameApplication)
                val calendarRepository = application.container.calendarRepository
                CalendarDetailViewModel(calendarRepository)
            }
        }
    }
}