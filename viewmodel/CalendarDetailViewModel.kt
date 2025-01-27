package com.feliii.alpvp.viewmodel

import android.os.Build
import android.util.Log
import android.widget.Toast
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
import com.feliii.alpvp.model.GeneralResponseModel
import com.feliii.alpvp.model.GetCalendarResponse
import com.feliii.alpvp.repository.CalendarRepository
import com.feliii.alpvp.uiStates.CalendarDetailDataStatusUIState
import com.feliii.alpvp.uiStates.StringDataStatusUIState
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

    var submissionStatus: StringDataStatusUIState by mutableStateOf(StringDataStatusUIState.Start)
        private set

    var dateChosen: String by mutableStateOf("")
        private set

    var moodChosen: List<Int> by mutableStateOf(emptyList())
        private set

    val selectableMood:Int
        get() = 3 - moodChosen.size

    var note: String by mutableStateOf("")
        private set

    fun changeNote(newNote: String){
        note = newNote
    }

    var dayOfWeek: String by mutableStateOf("")
        private set

    var monthYear: String by mutableStateOf("")
        private set

    fun selectMood(moodId: Int){
        moodChosen = if(moodChosen.contains(moodId)){
            moodChosen.toMutableList().apply { remove(moodId) }
        } else if(moodChosen.size < 3){
            moodChosen.toMutableList().apply { add(moodId) }
        } else {
            moodChosen
        }
    }

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
                            moodChosen = res.body()!!.data.moods.map { it - 1 }
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
    fun saveButton(token: String, navController: NavHostController) {
        if(moodChosen.size == 0 && note.equals("")){
            Toast.makeText(navController.context, "Please enter a note or select at least 1 mood", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        } else if(moodChosen.size == 0){
            Toast.makeText(navController.context, "Please select at least 1 mood", Toast.LENGTH_SHORT).show()
        } else {
            viewModelScope.launch{
                submissionStatus = StringDataStatusUIState.Loading


                Log.d("Entry-form", "TOKEN: ${token}")

                try {
                    val adjustedMoods = moodChosen.map { it + 1 }
                    val call = calendarRepository.createOrUpdate(token, dateChosen, note, adjustedMoods)

                    call.enqueue(object : Callback<GeneralResponseModel> {
                        override fun onResponse(
                            call: Call<GeneralResponseModel>,
                            res: Response<GeneralResponseModel>
                        ) {
                            if (res.isSuccessful) {
                                Log.d("json", "JSON RESPONSE: ${res.body()!!.data}")
                                submissionStatus = StringDataStatusUIState.Success(res.body()!!.data)

                                navController.navigate(PagesEnum.Calendar.name) {
                                    popUpTo(PagesEnum.Calendar.name) {
                                        inclusive = true
                                    }
                                }

                            }else{
                                val errorMessage = Gson().fromJson(
                                    res.errorBody()!!.charStream(),
                                    ErrorModel::class.java
                                )
                                submissionStatus = StringDataStatusUIState.Failed(errorMessage.errors)
                            }
                        }

                        override fun onFailure(call: Call<GeneralResponseModel>, t: Throwable) {
                            submissionStatus = StringDataStatusUIState.Failed(t.localizedMessage)
                        }

                    })
                }catch (error: IOException){
                    submissionStatus = StringDataStatusUIState.Failed(error.localizedMessage)
                }
            }
        }
    }

//    fun updateCalendarDetailData(token: String, id: Int){
//        viewModelScope.launch {
//            submissionStatus = StringDataStatusUIState.Loading
//
//            try{
//                val call = calendarRepository.updateEntry(token, id, dateChosen, note, moodChosen)
//
//                call.enqueue(object : Callback<GeneralResponseModel> {
//                    override fun onResponse(
//                        call: Call<GeneralResponseModel>,
//                        res: Response<GeneralResponseModel>
//                    ) {
//                        if(res.isSuccessful) {
//                            submissionStatus = StringDataStatusUIState.Success(res.body()!!.data)
//
//                        }else {
//                            val errorMessage = Gson().fromJson(
//                                res.errorBody()!!.charStream(),
//                                ErrorModel::class.java
//                            )
//                            submissionStatus = StringDataStatusUIState.Failed(errorMessage.errors)
//                        }
//                    }
//
//                    override fun onFailure(call: Call<GeneralResponseModel?>, t: Throwable) {
//                        TODO("Not yet implemented")
//                    }
//                })
//            }catch (error: IOException){
//                submissionStatus = StringDataStatusUIState.Failed(error.localizedMessage)
//            }
//        }
//    }
//
//    fun createEntryData(token: String, navController: NavHostController){
//        viewModelScope.launch {
//            submissionStatus = StringDataStatusUIState.Loading
//
//            Log.d("entry-form", "TOKEN: ${token}")
//
//            try{
//                val call = calendarRepository.createEntry(token, dateChosen, note, moodChosen)
//
//                call.enqueue(object : Callback<GeneralResponseModel> {
//                    override fun onResponse(
//                        call: Call<GeneralResponseModel>,
//                        res: Response<GeneralResponseModel>
//                    ) {
//                        if (res.isSuccessful) {
//                            Log.d("json", "JSON RESPONSE: ${res.body()!!.data}")
//                            submissionStatus = StringDataStatusUIState.Success(res.body()!!.data)
//
//                            navController.navigate(PagesEnum.Calendar.name)
//                        } else {
//                            val errorMessage = Gson().fromJson(
//                                res.errorBody()!!.charStream(),
//                                ErrorModel::class.java
//                            )
//                            submissionStatus = StringDataStatusUIState.Failed(errorMessage.errors)
//                        }
//                    }
//
//                    override fun onFailure(call: Call<GeneralResponseModel>, t: Throwable) {
//                        submissionStatus = StringDataStatusUIState.Failed(t.localizedMessage)
//                    }
//                })
//            }catch(error: IOException){
//                submissionStatus = StringDataStatusUIState.Failed(error.localizedMessage)
//            }
//        }
//    }





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