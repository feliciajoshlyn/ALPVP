package com.feliii.alpvp.uiStates

import com.feliii.alpvp.model.CalendarModel

sealed interface CalendarDataStatusUIState {
    data class Success(val data: List<CalendarModel>): CalendarDataStatusUIState
    object Loading: CalendarDataStatusUIState
    object Start: CalendarDataStatusUIState
    data class Failed(val errorMessage: String): CalendarDataStatusUIState
}