package com.feliii.alpvp.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.feliii.alpvp.GeneralResponseModel
import com.feliii.alpvp.service.UserAPIService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Call

//repository buat isi function cz in service itu cuma declare
interface UserRepository {
    val currentUserToken: Flow<String>
    val currentUsername: Flow<String>

    fun logout(token: String): Call<GeneralResponseModel>

    suspend fun saveUserToken(token: String)
    suspend fun saveUsername(username: String)
}

class NetworkUserRepository(
    private val userDataStore: DataStore<Preferences>,
    private val userAPIService: UserAPIService
): UserRepository {
    private companion object {
        val USERNAME = stringPreferencesKey("username")
        val USER_TOKEN = stringPreferencesKey("user_token")
    }

    override val currentUsername: Flow<String> = userDataStore.data.map { preferences ->
        preferences[USERNAME] ?: "Unknown"
    }

    override val currentUserToken: Flow<String> = userDataStore.data.map { preferences ->
        preferences[USER_TOKEN] ?: "Unknown"
    }

    override suspend fun saveUsername(username: String) {
        userDataStore.edit { preferences ->
            preferences[USERNAME] = username
        }
    }

    override suspend fun saveUserToken(token: String) {
        userDataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
        }
    }

    override fun logout(token: String): Call<GeneralResponseModel> {
        return userAPIService.logout(token)
    }
}