package com.example.reelygoodmovies.di

import android.content.Context
import com.example.reelygoodmovies.data.local_db.MovieDataBase
import com.example.reelygoodmovies.data.models.Movie
import com.example.reelygoodmovies.data.remote_db.MovieService
import com.example.reelygoodmovies.utils.Constants
import com.example.reelygoodmovies.utils.MovieDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor{ chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization","Bearer ${Constants.API_KEY}")
                .build()
                chain.proceed(request)
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson : Gson, okHttpClient: OkHttpClient) : Retrofit{
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient).build()
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Movie::class.java, MovieDeserializer())
            .create()
    }

    @Provides
    fun provideMovieService(retrofit : Retrofit) : MovieService =
        retrofit.create(MovieService::class.java)

    @Provides
    @Singleton
    fun provideLocalDatabase (@ApplicationContext appContext: Context) : MovieDataBase = MovieDataBase.getDataBase(appContext)

    @Provides
    @Singleton
    fun provideMovieDao(database: MovieDataBase) = database.movieDao()

    }
