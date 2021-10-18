package de.stenzel.tim.spieleabend.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.stenzel.tim.spieleabend.network.BoardgameApiService
import de.stenzel.tim.spieleabend.presentation.assistant.AssistantAdapter
import de.stenzel.tim.spieleabend.presentation.assistant.CatalogueAdapter
import de.stenzel.tim.spieleabend.presentation.events.EventAdapter
import de.stenzel.tim.spieleabend.presentation.news.NewsAdapter
import de.stenzel.tim.spieleabend.repositories.BoardgameRepository
import de.stenzel.tim.spieleabend.repositories.DefaultBoardgameRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //provide Adapter for Lists
    @Provides
    fun provideNewsAdapter() : NewsAdapter = NewsAdapter()

    @Provides
    fun provideEventsAdapter() : EventAdapter = EventAdapter()

    @Provides
    fun provideAssistantAdapter() : AssistantAdapter = AssistantAdapter()

    @Provides
    fun provideCatalogueAdapter(@ApplicationContext appContext: Context) : CatalogueAdapter = CatalogueAdapter(appContext)

    @Provides
    @Singleton
    fun provideBoardgameRepository(service: BoardgameApiService) = DefaultBoardgameRepository(service) as BoardgameRepository

}