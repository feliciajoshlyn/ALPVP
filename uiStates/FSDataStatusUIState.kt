package com.feliii.alpvp.uiStates

import com.feliii.alpvp.model.FidgetSpinnerModel

sealed interface FSDataStatusUIState {
    data class Success(val data: FidgetSpinnerModel): FSDataStatusUIState

    object Loading: FSDataStatusUIState
    object Start: FSDataStatusUIState
    data class Failed(val errorMessage: String): FSDataStatusUIState

    //sucess = bawa data, use data class cz object cant have a constructor -Felicia
}