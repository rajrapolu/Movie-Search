package com.testapp.coding.moviesearch

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.Toast
import com.testapp.coding.moviesearch.models.MovieSearchDTO
import kotlinx.android.synthetic.main.activity_movie_results.*

class MovieResultsActivity : AppCompatActivity() {

    private lateinit var mMovieSearchAdapter: MovieSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_results)

        movie_search_progress_bar.visibility = View.VISIBLE
        val viewModel = ViewModelProviders.of(this).get(MovieResultsViewModel::class.java)
        // View observes on ViewModels LiveData
        viewModel.getMovieResults().observe(this,
            Observer<MovieSearchDTO> { movieSearchDTO: MovieSearchDTO? ->
                updateResults(movieSearchDTO)
            })

        populateUI()
    }

    /**
     * Updates the movie results
     * @param movieSearchDTO DTO returned by the api
     */
    private fun updateResults(movieSearchDTO: MovieSearchDTO?) {
        if (movieSearchDTO == null) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
        } else {
            addContentToAdapter(movieSearchDTO)
        }
    }

    private fun addContentToAdapter(movieSearchDTO: MovieSearchDTO) {
        mMovieSearchAdapter.updateItems(movieSearchDTO.results)
        movie_search_progress_bar.visibility = View.GONE
    }

    private fun populateUI() {
        mMovieSearchAdapter = MovieSearchAdapter(mutableListOf())
        val gridLayoutManager = GridLayoutManager(this, 2)
        movie_search_recycler_view.layoutManager = gridLayoutManager
        movie_search_recycler_view.adapter = mMovieSearchAdapter
    }
}
