package de.stenzel.tim.spieleabend.di

import android.app.Application
import android.content.Context
import androidx.annotation.MainThread
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.stenzel.tim.spieleabend.helpers.Constants
import de.stenzel.tim.spieleabend.models.local.DominionDao
import de.stenzel.tim.spieleabend.models.local.DominionDatabase
import de.stenzel.tim.spieleabend.network.BoardgameApiService
import de.stenzel.tim.spieleabend.presentation.assistant.AssistantAdapter
import de.stenzel.tim.spieleabend.presentation.assistant.CatalogueAdapter
import de.stenzel.tim.spieleabend.presentation.assistant.DominionAdapter
import de.stenzel.tim.spieleabend.presentation.events.EventAdapter
import de.stenzel.tim.spieleabend.presentation.news.NewsAdapter
import de.stenzel.tim.spieleabend.repositories.BoardgameRepository
import de.stenzel.tim.spieleabend.repositories.DefaultBoardgameRepository
import de.stenzel.tim.spieleabend.repositories.DefaultDominionRepository
import de.stenzel.tim.spieleabend.repositories.DominionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBoardgameRepository(service: BoardgameApiService) = DefaultBoardgameRepository(service) as BoardgameRepository

    @Provides
    @Singleton
    fun provideDefaultDominionRepository(dominionDao: DominionDao) = DefaultDominionRepository(dominionDao) as DominionRepository

}