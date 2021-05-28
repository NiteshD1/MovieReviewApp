package com.studyquiz.ratemovie.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.studyquiz.ratemovie.R
import com.studyquiz.ratemovie.ui.MovieViewModel
import com.google.android.material.snackbar.Snackbar
import com.studyquiz.ratemovie.databinding.FragmentMovieBinding
import com.studyquiz.ratemovie.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_movie_preview.view.*


@AndroidEntryPoint
class MovieFragment : Fragment(R.layout.fragment_movie) {

    val viewModel: MovieViewModel by viewModels()
    val args: MovieFragmentArgs by navArgs()

    private var _binding: FragmentMovieBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        val view = binding.root

        setupAllViews()

        return view
    }

    private fun setupAllViews() {
        // safe arguments
        val movie = args.movie

        binding.apply {
            tvTitle.text = movie.title
            tvDescription.text = movie.overview
            tvUpvote.text = movie.voteCount.toString()
            tvPopularity.text = movie.popularity.toString()
            ratingBar.rating = movie.rating.toFloat()
            Glide.with(this@MovieFragment).load(Constants.IMAGE_BEGIN_URL + movie.posterPath).into(ivPoster)
        }

        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, ratingFloatData, b ->
           val rateLocal =  ratingBar.rating
            movie.rating = rateLocal.toInt()
            viewModel.saveRatedMovie(movie)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}