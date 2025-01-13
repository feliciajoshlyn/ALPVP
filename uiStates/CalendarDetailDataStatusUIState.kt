package com.feliii.alpvp.uiStates

import com.feliii.alpvp.model.CalendarModel

sealed interface CalendarDetailDataStatusUIState {
    data class Success(val data: CalendarModel): CalendarDetailDataStatusUIState
    object Loading: CalendarDetailDataStatusUIState
    object Start: CalendarDetailDataStatusUIState
    data class Failed(val errorMessage: String): CalendarDetailDataStatusUIState
}