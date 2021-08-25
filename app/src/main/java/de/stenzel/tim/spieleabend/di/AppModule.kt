package de.stenzel.tim.spieleabend.di

import android.app.Application
import android.provider.SyncStateContract
import androidx.room.PrimaryKey
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.stenzel.tim.spieleabend.helpers.Constants
import de.stenzel.tim.spieleabend.network.BoardgameApiService
import de.stenzel.tim.spieleabend.notifications.MyFirebaseMessagingService
import de.stenzel.tim.spieleabend.presentation.assistant.AssistantAdapter
import de.stenzel.tim.spieleabend.presentation.events.EventAdapter
import de.stenzel.tim.spieleabend.presentation.news.NewsAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance() : BoardgameApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_BOARDGAME_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BoardgameApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFireDB() : FirebaseDatabase = FirebaseDatabase.getInstance(Constants.URL_FIRE_DB)

    //provide Adapter for Lists
    @Provides
    fun provideNewsAdapter() : NewsAdapter = NewsAdapter()

    @Provides
    fun provideEventsAdapter() : EventAdapter = EventAdapter()

    @Provides
    fun provideAssistantAdapter() : AssistantAdapter = AssistantAdapter()

}