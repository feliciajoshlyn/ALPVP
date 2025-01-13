package com.feliii.alpvp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_data"
)

//initialize container used
class RelaxGameApplication : Application(){
    lateinit var container: AppContainer  
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(dataStore)
    }
}