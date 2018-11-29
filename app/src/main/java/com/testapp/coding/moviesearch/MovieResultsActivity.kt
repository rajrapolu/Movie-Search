package com.testapp.coding.moviesearch

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.testapp.coding.moviesearch.models.MovieSearchDTO
import kotlinx.android.synthetic.main.activity_movie_results.*

class MovieResultsActivity : AppCompatActivity() {

    private lateinit var mMovieSearchAdapter: MovieSearchAdapter
    private lateinit var mViewModel: MovieResultsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_results)

        movie_search_progress_bar.visibility = View.VISIBLE
        mViewModel = ViewModelProviders.of(this).get(MovieResultsViewModel::class.java)
        // View observes on ViewModels LiveData
        // This is used to observe changes on discover movie results
        mViewModel.getMovieResults().observe(this,
            Observer<MovieSearchDTO> { movieSearchDTO: MovieSearchDTO? ->
                updateResults(movieSearchDTO, false)
            })

        populateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search_movie).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        return true
    }

    override fun onNewIntent(intent: Intent?) {
        intent?.let {
            // By default the intent would not be set and getIntent would return the previous intent when activity was
            // created so set the intent so that whenever getIntent is called an updated copy is received
            setIntent(intent)
            handleIntent(intent)
        }
    }

    /**
     * When ever the user searches a query make a network call to get the results
     */
    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH.equals(intent.action)) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            Toast.makeText(this, query, Toast.LENGTH_LONG).show()
            mViewModel.getMovieSearchResults().observe(this,
                Observer<MovieSearchDTO> { movieSearchDTO: MovieSearchDTO? ->
                    updateResults(movieSearchDTO, true)
                    movie_search_progress_bar.visibility = View.GONE
                })
            movie_search_progress_bar.visibility = View.VISIBLE
            mViewModel.makeMovieSearchResultsCall(query)
        }
    }

    /**
     * Updates the movie results
     * @param movieSearchDTO DTO returned by the api
     */
    private fun updateResults(movieSearchDTO: MovieSearchDTO?, clearItems: Boolean) {
        if (movieSearchDTO == null) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
        } else {
            addContentToAdapter(movieSearchDTO, clearItems)
        }
    }

    private fun addContentToAdapter(movieSearchDTO: MovieSearchDTO, clearItems: Boolean) {
        mMovieSearchAdapter.updateItems(movieSearchDTO.results, clearItems)
        movie_search_progress_bar.visibility = View.GONE
    }

    private fun populateUI() {
        mMovieSearchAdapter = MovieSearchAdapter(mutableListOf())
        val linearLayoutManager = LinearLayoutManager(this)
        movie_search_recycler_view.layoutManager = linearLayoutManager
        movie_search_recycler_view.adapter = mMovieSearchAdapter
    }
}
