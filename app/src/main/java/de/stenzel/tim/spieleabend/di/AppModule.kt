package de.stenzel.tim.spieleabend.di

import android.provider.SyncStateContract
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
    fun provideFireDB() : DatabaseReference = FirebaseDatabase.getInstance(Constants.URL_FIRE_DB).reference

    //TODO provide Adapter for Lists
    @Provides
    fun provideNewsAdapter() : NewsAdapter = NewsAdapter()


}