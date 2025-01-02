package com.feliii.alpvp.repository

import com.feliii.alpvp.model.CalendarRequest
import com.feliii.alpvp.model.GeneralResponseModel
import com.feliii.alpvp.model.GetAllCalendarResponses
import com.feliii.alpvp.model.GetCalendarResponse
import com.feliii.alpvp.service.CalendarAPIService
import retrofit2.Call

interface CalendarRepository {
    fun getEntryList(token: String): Call<GetAllCalendarResponses>

    fun getEntry(token: String, date: String): Call<GetCalendarResponse>

    fun createEntry(token: String, date: String, note: String, moods: List<Int>): Call<GeneralResponseModel>

    fun updateEntry(token: String, entryId: Int, date: String, note: String, moods: List<Int>): Call<GeneralResponseModel>

    fun createOrUpdate(token: String, date: String, note: String, moods: List<Int>): Call<GeneralResponseModel>
}

class NetworkCalendarRepository (
    private val calendarAPIService: CalendarAPIService
): CalendarRepository{
    override fun getEntryList(token: String): Call<GetAllCalendarResponses> {
        return calendarAPIService.getEntryList(token)
    }

    override fun getEntry(token: String, date: String): Call<GetCalendarResponse> {
        return calendarAPIService.getEntry(token, date)
    }

    override fun createEntry(
        token: String,
        date: String,
        note: String,
        moods: List<Int>
    ): Call<GeneralResponseModel> {
        return calendarAPIService.createEntry(token, CalendarRequest(date, note, moods))
    }

    override fun updateEntry(
        token: String,
        entryId: Int,
        date: String,
        note: String,
        moods: List<Int>
    ): Call<GeneralResponseModel> {
        return calendarAPIService.updateEntry(token, entryId, CalendarRequest(date, note, moods))
    }

    override fun createOrUpdate(
        token: String,
        date: String,
        note: String,
        moods: List<Int>
    ): Call<GeneralResponseModel>{
        return calendarAPIService.createOrUpdate(token, CalendarRequest(date, note, moods))
    }
}