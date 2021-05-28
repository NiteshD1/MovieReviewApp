package com.studyquiz.ratemovie.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.studyquiz.ratemovie.R
import com.studyquiz.ratemovie.adapters.MovieAdapter
import com.studyquiz.ratemovie.ui.MovieViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_saved_rated_movie.*

@AndroidEntryPoint
class SavedRatedMoviesFragment : Fragment(R.layout.fragment_saved_rated_movie) {

    val viewModel: MovieViewModel by viewModels()
    lateinit var movieAdapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        movieAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
            }
            findNavController().navigate(
                    R.id.action_savedRatedMoviesFragment_to_movieFragment,
                    bundle
            )
        }



        viewModel.getSavedRatedMovies().observe(viewLifecycleOwner, Observer { movies ->
            movieAdapter.differ.submitList(movies)
        })
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter()
        rvRatedMovies.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}