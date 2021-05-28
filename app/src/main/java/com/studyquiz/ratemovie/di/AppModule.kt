package com.studyquiz.ratemovie.di

import android.content.Context
import androidx.room.Room
import com.studyquiz.ratemovie.api.MoviesAPI
import com.studyquiz.ratemovie.db.MovieDao
import com.studyquiz.ratemovie.db.MovieDatabase
import com.studyquiz.ratemovie.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context):Context{
        return context
    }


    // providing room dependencies
    @Singleton
    @Provides
    fun provideMovieDatabase(context:Context): MovieDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            "movie_result_db.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(movieDatabase: MovieDatabase): MovieDao{
        return movieDatabase.getMovieDao()
    }


    // providing retrofit dependencies

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit{

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

       return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsAPI(retrofit: Retrofit): MoviesAPI{
        return retrofit.create(MoviesAPI::class.java)
    }
}