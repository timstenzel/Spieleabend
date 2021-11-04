package de.stenzel.tim.spieleabend.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.stenzel.tim.spieleabend.helpers.Constants
import de.stenzel.tim.spieleabend.models.local.DominionDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDominionDatabase(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        appContext, DominionDatabase::class.java, Constants.DB_NAME)
        .build()

    @Provides
    @Singleton
    fun provideDominionDao(database: DominionDatabase) = database.dominionDao()
}