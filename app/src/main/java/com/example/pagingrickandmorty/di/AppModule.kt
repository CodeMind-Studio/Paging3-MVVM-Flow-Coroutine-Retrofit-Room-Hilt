package com.example.pagingrickandmorty.di

import android.content.Context
import androidx.room.Room
import com.example.pagingrickandmorty.dao.KeyDao
import com.example.pagingrickandmorty.dao.PersonDao
import com.example.pagingrickandmorty.database.PersonDatabase
import com.example.pagingrickandmorty.network.PersonApi
import com.example.pagingrickandmorty.utils.Constants
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideApi(): PersonApi {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            val request: Request = chain.request().newBuilder().addHeader(
                "api-key",
                ""
            ).build()
            chain.proceed(request)

        }).addInterceptor(interceptor = logging).connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create()
        return Retrofit.Builder().baseUrl(Constants.URL).client(client)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
            .create(PersonApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PersonDatabase {
        return Room.databaseBuilder(context, PersonDatabase::class.java, "personData").build()
    }
}