package de.stenzel.tim.spieleabend.di

import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.stenzel.tim.spieleabend.helpers.Constants
import de.stenzel.tim.spieleabend.helpers.isNetworkAvailable
import de.stenzel.tim.spieleabend.network.BoardgameApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHTTPClientInstance(@ApplicationContext context: Context) : OkHttpClient {
        return OkHttpClient.Builder()
            .cache(Cache(context.cacheDir, (5*1024*1024).toLong()))
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (isNetworkAvailable(context)) {
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                } else {
                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60*60*24*7).build()
                }
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(okHttpClient: OkHttpClient) : BoardgameApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_BOARDGAME_API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(BoardgameApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFireDB() : FirebaseDatabase = FirebaseDatabase.getInstance(Constants.URL_FIRE_DB)

}