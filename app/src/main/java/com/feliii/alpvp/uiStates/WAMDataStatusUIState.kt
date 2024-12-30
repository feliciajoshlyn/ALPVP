package com.feliii.alpvp.uiStates

import com.feliii.alpvp.model.WhackAMoleModel

sealed interface WAMDataStatusUIState {
    data class Success(val data: WhackAMoleModel): WAMDataStatusUIState
    //success -> bawa data?
    object Loading: WAMDataStatusUIState
    object Start: WAMDataStatusUIState
    data class Failed(val errorMessage: String): WAMDataStatusUIState
    //use data class cz object cant have a constructor..
}