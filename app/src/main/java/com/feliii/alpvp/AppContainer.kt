package com.feliii.alpvp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.feliii.alpvp.repository.AuthenticationRepository
import com.feliii.alpvp.repository.CalendarRepository
import com.feliii.alpvp.repository.UserRepository
import com.feliii.alpvp.repository.NetworkAuthenticationRepository
import com.feliii.alpvp.repository.NetworkCalendarRepository
import com.feliii.alpvp.repository.NetworkUserRepository
import com.feliii.alpvp.repository.NetworkWAMRepository
import com.feliii.alpvp.repository.WAMRepository
import com.feliii.alpvp.service.AuthenticationAPIService
import com.feliii.alpvp.service.CalendarAPIService
import com.feliii.alpvp.service.UserAPIService
import com.feliii.alpvp.service.WAMAPIService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.NetworkInterface

//container is for bengun service, tiap service & repo baru didaftarin di sini
interface AppContainer {
    val authenticationRepository: AuthenticationRepository
    val userRepository: UserRepository
    val wamRepository: WAMRepository
    val calendarRepository: CalendarRepository
}

class DefaultAppContainer(private val userDataStore: DataStore<Preferences>) : AppContainer {
    private val APIBaseURL = "http://192.168.1.3:3000/" //isi ip wifi

//    val ipAddress = getDeviceIPAddress()
//    private val APIBaseURL = "http://$ipAddress:3000/"

    private val authenticationRetrofitService: AuthenticationAPIService by lazy {
        val retrofit = initRetrofit()

        retrofit.create(AuthenticationAPIService::class.java)
    }

    private val userRetrofitService: UserAPIService by lazy {
        val retrofit = initRetrofit()

        retrofit.create(UserAPIService::class.java)
    }

    private val wamRetrofitService: WAMAPIService by lazy {
        val retrofit = initRetrofit()

        retrofit.create(WAMAPIService::class.java)
    }

    private val CalendarRetrofitService: CalendarAPIService by lazy {
        val retrofit = initRetrofit()

        retrofit.create(CalendarAPIService::class.java)
    }

    override val authenticationRepository: AuthenticationRepository by lazy {
        NetworkAuthenticationRepository(authenticationRetrofitService)
    }

    override val userRepository: UserRepository by lazy {
        NetworkUserRepository(userDataStore, userRetrofitService)
    }
    override val wamRepository: WAMRepository by lazy {
        NetworkWAMRepository(wamRetrofitService)
    }

    override val calendarRepository: CalendarRepository by lazy {
        NetworkCalendarRepository(CalendarRetrofitService)
    }

    //to initialize retrofit
    private fun initRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
        client.addInterceptor(logging)

        return Retrofit
            .Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .client(client.build())
            .baseUrl(APIBaseURL)
            .build()
    }
}

fun getDeviceIPAddress(): String {
    return try {
        NetworkInterface.getNetworkInterfaces().asSequence()
            .flatMap { it.inetAddresses.asSequence() }
            .firstOrNull { !it.isLoopbackAddress && it.hostAddress.contains(".") }
            ?.hostAddress ?: "Unknown IP"
    } catch (e: Exception) {
        "Error: ${e.localizedMessage}"
    }
}