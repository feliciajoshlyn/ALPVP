package com.feliii.alpvp.uiStates

import com.feliii.alpvp.model.CalendarModel

sealed interface CalendarDetailDataUIState {
    data class Success(val data: CalendarModel): CalendarDetailDataUIState
    object Loading: CalendarDetailDataUIState
    object Start: CalendarDetailDataUIState
    data class Failed(val errorMessage: String): CalendarDetailDataUIState
}