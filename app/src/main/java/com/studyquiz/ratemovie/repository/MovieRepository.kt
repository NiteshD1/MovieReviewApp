package com.studyquiz.ratemovie.repository

import com.studyquiz.ratemovie.api.MoviesAPI
import com.studyquiz.ratemovie.db.MovieDao
import com.studyquiz.ratemovie.models.Movie
import javax.inject.Inject


class MovieRepository @Inject constructor(
    val movieDao: MovieDao,
    val moviesAPI: MoviesAPI
) {
    suspend fun getMoviesList(countryCode: String, pageNumber: Int) =
        moviesAPI.getMoviesList(countryCode, pageNumber)

//    suspend fun searchNews(movieId: String, pageNumber: Int) =
//        moviesAPI.getMovieById(movieId, pageNumber)

    suspend fun upsert(movie: Movie) = movieDao.upsert(movie)

    fun getSavedRatedMovies() = movieDao.getAllMovies()

}