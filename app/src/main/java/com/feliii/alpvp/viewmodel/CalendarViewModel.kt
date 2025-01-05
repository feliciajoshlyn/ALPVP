package com.feliii.alpvp.viewmodel

import android.annotation.SuppressLint
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
import com.feliii.alpvp.RelaxGameApplication
import com.feliii.alpvp.enums.PagesEnum
import com.feliii.alpvp.model.ErrorModel
import com.feliii.alpvp.model.GetAllCalendarResponses
import com.feliii.alpvp.repository.CalendarRepository
import com.feliii.alpvp.uiStates.CalendarDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.time.LocalDate
import java.time.YearMonth


@SuppressLint("NewApi")
class CalendarViewModel(
    private val calendarRepository: CalendarRepository
) : ViewModel() {
    var dataStatus: CalendarDataStatusUIState by mutableStateOf(CalendarDataStatusUIState.Start)

    var currentMonth by mutableStateOf(YearMonth.now())
        private set

    var selectedDate by mutableStateOf(LocalDate.now())
        private set

    private val _daysList = MutableStateFlow<List<String>>(emptyList())
    val daysList: StateFlow<List<String>> = _daysList

    init {
        updateDaysList()
    }

    fun changeMonth(newMonth: YearMonth) {
        currentMonth = newMonth
        updateDaysList()
        // Reset selected date to the first day of the new month if needed
        selectedDate = newMonth.atDay(1)
    }

    fun selectDate(date: LocalDate) {
        selectedDate = date
    }

    var isFutureDate by mutableStateOf(false)
        private set

    fun checkFutureDate(): Boolean {
        return selectedDate.isAfter(LocalDate.now())
    }

    private fun updateDaysList() {
        val daysInMonth = currentMonth.lengthOfMonth()
        val firstDayOfMonth = (currentMonth.atDay(1).dayOfWeek.value + 6) % 7

        val list = List(42) { index ->
            if (index >= firstDayOfMonth && index < firstDayOfMonth + daysInMonth) {
                (index - firstDayOfMonth + 1).toString()
            } else {
                ""
            }
        }
        _daysList.value = list
    }

    fun plusMonth() {
        changeMonth(currentMonth.plusMonths(1))
    }

    fun minusMonth() {
        changeMonth(currentMonth.minusMonths(1))
    }


    fun getCalendarData(token: String, navController: NavHostController) {
        viewModelScope.launch {
            dataStatus = CalendarDataStatusUIState.Loading

            try {
                val call = calendarRepository.getEntryList(token)

                call.enqueue(object : Callback<GetAllCalendarResponses> {
                    override fun onResponse(
                        call: Call<GetAllCalendarResponses>,
                        res: Response<GetAllCalendarResponses>
                    ) {
                        if(res.isSuccessful){
                            dataStatus = CalendarDataStatusUIState.Success(res.body()!!.data)

                            Log.d("get-calendar-data", "GET CALENDAR: ${res.body()}")
                        }else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )

                            dataStatus = CalendarDataStatusUIState.Failed(errorMessage.errors)
                        }
                    }
                    override fun onFailure(call: Call<GetAllCalendarResponses?>, t: Throwable) {
                        dataStatus = CalendarDataStatusUIState.Failed(t.localizedMessage)
                    }
                })
            }catch(error: IOException){
                dataStatus = CalendarDataStatusUIState.Failed(error.localizedMessage)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as RelaxGameApplication)
                val calendarRepository = application.container.calendarRepository
                CalendarViewModel(calendarRepository)
            }
        }
    }
}
