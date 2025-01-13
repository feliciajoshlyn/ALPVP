package com.feliii.alpvp.model

data class GetAllCalendarResponses(
    val data: List<CalendarModel>
)

data class GetCalendarResponse(
    val data: CalendarModel
)

data class CalendarModel(
    val id: Int,
    val date: String,
    val note: String,
    val moods: List<Int>
)

data class CalendarRequest(
    val date: String,
    val note: String,
    val moods: List<Int>
)