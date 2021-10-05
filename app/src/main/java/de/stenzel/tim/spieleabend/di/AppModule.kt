package de.stenzel.tim.spieleabend.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.stenzel.tim.spieleabend.presentation.assistant.AssistantAdapter
import de.stenzel.tim.spieleabend.presentation.events.EventAdapter
import de.stenzel.tim.spieleabend.presentation.news.NewsAdapter

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

}