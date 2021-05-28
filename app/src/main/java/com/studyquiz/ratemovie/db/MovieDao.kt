package com.studyquiz.ratemovie.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.studyquiz.ratemovie.models.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(movie: Movie): Long

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Movie>>


}