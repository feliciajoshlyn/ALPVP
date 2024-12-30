package com.feliii.alpvp.service

import com.feliii.alpvp.GeneralResponseModel
import com.feliii.alpvp.model.CalendarRequest
import com.feliii.alpvp.model.GetAllCalendarResponses
import com.feliii.alpvp.model.GetCalendarResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CalendarAPIService {
    @GET("api/entry")
    fun getEntryList(@Header("X-API-TOKEN") token: String): Call<GetAllCalendarResponses>

    @GET("api/entry-detail")
    fun getEntry(@Header("X-API-TOKEN") token: String): Call<GetCalendarResponse>

    @POST("api/entry")
    fun createEntry(@Header("X-API-TOKEN") token: String, @Body calendarModel: CalendarRequest): Call<GeneralResponseModel>

    @PUT("api/entry/{id}")
    fun updateEntry(@Header("X-API-TOKEN") token: String, @Path("id") entryId: Int, @Body calendarModel: CalendarRequest): Call<GeneralResponseModel>
}

//protectedRouter.post("/api/entry", CalendarController.createEntry)
//protectedRouter.get("/api/entry", CalendarController.getEntryList)
//protectedRouter.get("/api/entry-detail", CalendarController.getEntry)
//protectedRouter.put("/api/entry/:calendar_id", CalendarController.updateEntry)