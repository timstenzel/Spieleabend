package de.stenzel.tim.spieleabend.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.stenzel.tim.spieleabend.models.local.DominionDao
import de.stenzel.tim.spieleabend.network.BoardgameApiService
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
    fun provideDefaultDominionRepository(dominionDao: DominionDao) = DefaultDominionRepository(dominionDao) as DominionRepository

    @Provides
    @Singleton
    fun provideBoardgameRepository(service: BoardgameApiService) = DefaultBoardgameRepository(service) as BoardgameRepository


}