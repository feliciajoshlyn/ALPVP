package com.feliii.alpvp.viewmodel

import android.content.Context
import com.feliii.alpvp.R

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.lazy.grid.GridCells
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
import com.feliii.alpvp.model.GeneralResponseModel
import com.feliii.alpvp.RelaxGameApplication
import com.feliii.alpvp.enums.PagesEnum
import com.feliii.alpvp.model.ErrorModel
import com.feliii.alpvp.model.GetWAMResponse
import com.feliii.alpvp.model.WhackAMoleModel
import com.feliii.alpvp.repository.WAMRepository
import com.feliii.alpvp.uiStates.StringDataStatusUIState
import com.feliii.alpvp.uiStates.WAMDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class WAMViewModel (
    private val wamRepository: WAMRepository
): ViewModel() {
    var dataStatus: WAMDataStatusUIState by mutableStateOf(WAMDataStatusUIState.Start)
        private set

    var mode by mutableStateOf("")
        private set

    var score by mutableStateOf(0)
        private set

    var mole_chosen by mutableStateOf("")
        private set

    var song_chosen by mutableStateOf("")
        private set

    var timed_highscore by mutableStateOf(0)
        private set

    var endless_highscore by mutableStateOf(0)
        private set

    var intense_highscore by mutableStateOf(0)
        private set

    var highscore by mutableStateOf(0)
        private set

    //game things
    var activeMole by mutableStateOf(-1)
        private set

    var timeRemaining by mutableStateOf(30)
        private set

    var moleAppearanceDelay by mutableStateOf(1000L)
        private set

    var gridSize by mutableStateOf(3)
        private set

    var gridCells by mutableStateOf(GridCells.Fixed(gridSize))
        private set

    var isTimedMode by mutableStateOf(false)
        private set

    var gameIsOver by mutableStateOf(false)
        private set

    var submissionStatus: StringDataStatusUIState by mutableStateOf(StringDataStatusUIState.Start)
        private set

    private var moleGenerationJob: Job? = null
    private var timerJob: Job? = null

    var chosenSong by mutableStateOf(Song("Burrow Bliss", R.raw.burrowbliss))
        private set

    fun setChosenSong(songName: String){
        for(song in songList){
            if(song.name == songName) {
                chosenSong = song
                return
            }
        }
    }

    val songList = listOf(
        Song("Burrow Bliss", R.raw.burrowbliss),
        Song("Burrow Bliss V2", R.raw.burrowblissv2),
        Song("Mole's Calm", R.raw.molescalm),
        Song("Mole's Calm V2", R.raw.molescalmv2),
        Song("Quiet Burrows", R.raw.quietburrows)
    )

    private var mediaPlayer: MediaPlayer? = null

    fun startSong(context: Context) {
        stopSong()
        boolIsPlaying = true
        mediaPlayer = MediaPlayer.create(context, chosenSong.id).apply {
            start()
        }
    }

    fun stopSong(){
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun onToggleSong(){
        boolIsPlaying = !boolIsPlaying
        if(mediaPlayer == null){
            return
        }
        if(isPlaying()){
            mediaPlayer?.pause()
        }else{
            mediaPlayer?.start()
        }
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }

    var boolIsPlaying by mutableStateOf(true)

    fun skipSong(context: Context){
        val index = songList.indexOf(chosenSong)
        if(index == songList.size - 1){
            chosenSong = songList[0]
        }else {
            chosenSong = songList[index + 1]
        }
        startSong(context)
    }

    fun leaveMenu(){
        stopSong()
    }

    fun updateWAMData(token: String, getWAM: () -> Unit) {
        viewModelScope.launch {
            submissionStatus = StringDataStatusUIState.Loading

            try {
                val call = wamRepository.updateWAMData(token, mole_chosen, song_chosen, timed_highscore, endless_highscore, intense_highscore)

                call.enqueue(object : Callback<GeneralResponseModel> {
                    override fun onResponse(
                        call: Call<GeneralResponseModel>,
                        res: Response<GeneralResponseModel>
                    ) {
                        if (res.isSuccessful) {
                            submissionStatus = StringDataStatusUIState.Success(res.body()!!.data)

                            getWAM()

                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            submissionStatus = StringDataStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(call: Call<GeneralResponseModel?>, t: Throwable) {
                        submissionStatus = StringDataStatusUIState.Failed(t.localizedMessage)
                    }
                })
            } catch (error: IOException) {
                submissionStatus = StringDataStatusUIState.Failed(error.localizedMessage)
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as RelaxGameApplication)
                val wamRepository = application.container.wamRepository
                WAMViewModel(wamRepository)
            }
        }
    }

    fun navigateToGame(navContoller: NavHostController, gameMode: String, wamModel: WhackAMoleModel){
        mode = gameMode
        score = 0
        mole_chosen = wamModel.mole_chosen
        song_chosen = wamModel.song_chosen
        timed_highscore = wamModel.timed_highscore
        endless_highscore = wamModel.endless_highscore
        intense_highscore = wamModel.intense_highscore

        if(gameMode == "timed"){
            highscore = wamModel.timed_highscore
            isTimedMode = true
            gridSize = 3
        }else if(gameMode == "endless"){
            highscore = wamModel.endless_highscore
            gridSize = 3
        }else if(gameMode == "intense"){
            highscore = wamModel.intense_highscore
            moleAppearanceDelay = 500L
            gridSize = 4
            isTimedMode = true
        }

        navContoller.navigate(PagesEnum.WhackAMoleGame.name) {
            popUpTo(PagesEnum.WhackAMoleMenu.name) {
                inclusive = true
            }
        }
    }


    fun startGame(){
        stopGame()

        moleGenerationJob = viewModelScope.launch {
            while(isActive && (timeRemaining > 0 || !isTimedMode)){
                delay(moleAppearanceDelay)
                mole_image_id = R.drawable.mole
                activeMole = (0 until gridSize*gridSize).random()
            }
        }

        if(isTimedMode){
            timerJob = viewModelScope.launch {
                while(isActive && timeRemaining > 0){
                    delay(1000L)
                    timeRemaining--
                }

                if(timeRemaining == 0){
                    stopGame()
                    gameOver()
                }
            }
        }
    }

    fun isMoleAtIndex(Index: Int): Boolean {
        return Index == activeMole
    }

    fun onMoleClick(index: Int){
        if(index == activeMole) {
            score++
            mole_image_id = R.drawable.mole_hit
            activeMole = -1
        }
    }

    var mole_image_id by mutableStateOf(R.drawable.mole)

    fun stopGame(){
        moleGenerationJob?.cancel()
        timerJob?.cancel()
        activeMole = -1
    }

    fun gameOver(){
        stopGame()


        if(score > highscore){
            highscore = score
            if(mode.equals("timed")){
                timed_highscore = highscore
            }else if(mode.equals("endless")){
                endless_highscore = highscore
            }else if(mode.equals("intense")){
                intense_highscore = highscore
            }
        }


        gameIsOver = true
    }

    fun backToMenu(token: String, navController: NavHostController, getWAM: () -> Unit){
        score = 0
        timeRemaining = 30
        gameIsOver = false
        updateWAMData(token, getWAM)
        navController.navigate(PagesEnum.WhackAMoleMenu.name) {
            popUpTo(PagesEnum.WhackAMoleGame.name) {
                inclusive = true
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopGame()
    }
}

data class Song(
    val name : String,
    val id : Int
)