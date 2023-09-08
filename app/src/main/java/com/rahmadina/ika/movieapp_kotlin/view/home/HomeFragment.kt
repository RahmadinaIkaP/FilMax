package com.rahmadina.ika.movieapp_kotlin.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahmadina.ika.movieapp_kotlin.R
import com.rahmadina.ika.movieapp_kotlin.data.model.movie.ResultPopularMovie
import com.rahmadina.ika.movieapp_kotlin.data.model.movie.trending.ResultTrendingMovies
import com.rahmadina.ika.movieapp_kotlin.data.model.tvshows.ResultPopularTvShows
import com.rahmadina.ika.movieapp_kotlin.data.network.ApiResponse
import com.rahmadina.ika.movieapp_kotlin.databinding.FragmentHomeBinding
import com.rahmadina.ika.movieapp_kotlin.utils.toast
import com.rahmadina.ika.movieapp_kotlin.view.home.adapter.PopularMovieAdapter
import com.rahmadina.ika.movieapp_kotlin.view.home.adapter.PopularTvShowsAdapter
import com.rahmadina.ika.movieapp_kotlin.view.home.adapter.TrendingMovieAdapter
import com.rahmadina.ika.movieapp_kotlin.view.movieandtvshows.viewmodel.MovieViewModel
import com.rahmadina.ika.movieapp_kotlin.view.movieandtvshows.viewmodel.TvShowsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), PopularMovieAdapter.PopularMoviesInterface,
    PopularTvShowsAdapter.PopularTvInterface, TrendingMovieAdapter.TrendingMoviesInterface {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val vmMovie : MovieViewModel by viewModels()
    private val vmTvShows : TvShowsViewModel by viewModels()
    private lateinit var pmAdapter : PopularMovieAdapter
    private lateinit var ptvAdapter : PopularTvShowsAdapter
    private lateinit var tmAdapter : TrendingMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSearch()
        setPopularMovie()
        setPopularTvShows()
        setTrendingMovie()
    }

    private fun setSearch() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.etSearch.text.isNullOrEmpty()) {
                    toast("Please type the keyword")
                } else {
                    val bundle = Bundle()
                    bundle.putString("keyword", binding.etSearch.text.toString())
                    findNavController().navigate(
                        R.id.action_homeFragment_to_searchResultFragment,
                        bundle
                    )
                }
            }
            false
        }
    }

    private fun setTrendingMovie() {
        vmMovie.getTrendingMovie()
        vmMovie.getTrendingMovieObserver().observe(viewLifecycleOwner){response ->
            when(response){
                is ApiResponse.Success -> {
                    response.data?.let { showRvTrendingMovies(it.results) }
                }
                is ApiResponse.Loading -> {

                }
                is ApiResponse.Error -> {
                    toast(response.msg.toString())
                }
            }
        }
    }

    private fun showRvTrendingMovies(data: List<ResultTrendingMovies>) {
        tmAdapter = TrendingMovieAdapter(this)
        tmAdapter.setData(data)

        binding.apply {
            rvTrending.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvTrending.adapter = tmAdapter
        }
    }

    private fun setPopularMovie() {
        vmMovie.getPopularMovie()
        vmMovie.getPopularMovieObserver().observe(viewLifecycleOwner){response ->
            when(response){
                is ApiResponse.Success -> {
                    response.data?.let { showRvPopularMovies(it.results) }
                }
                is ApiResponse.Loading -> {
                    
                }
                is ApiResponse.Error -> {
                    toast(response.msg.toString())
                }
            }
        }
    }

    private fun setPopularTvShows() {
        vmTvShows.getPopularTvShows()
        vmTvShows.getPopularTvObserver().observe(viewLifecycleOwner){response ->
            when(response){
                is ApiResponse.Success -> {
                    response.data?.let { showRvPopularTvShows(it.results) }
                }
                is ApiResponse.Loading -> {

                }
                is ApiResponse.Error -> {
                    toast(response.msg.toString())
                }
            }
        }
    }

    private fun showRvPopularTvShows(data: List<ResultPopularTvShows>) {
        ptvAdapter = PopularTvShowsAdapter(this)
        ptvAdapter.setData(data)

        binding.apply {
            rvPopularTv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvPopularTv.adapter = ptvAdapter
        }
    }

    private fun showRvPopularMovies(data: List<ResultPopularMovie>) {
        pmAdapter = PopularMovieAdapter(this)
        pmAdapter.setData(data)

        binding.apply {
            rvPopularMovies.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvPopularMovies.adapter = pmAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(resultPopularMovie: ResultPopularMovie) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToDetailMovieFragment(resultPopularMovie.id)
        findNavController().navigate(action)
    }

    override fun onItemClick(resultPopularTvShows: ResultPopularTvShows) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToDetailTvShowFragment(resultPopularTvShows.id)
        findNavController().navigate(action)
    }

    override fun onItemClick(resultTrendingMovies: ResultTrendingMovies) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToDetailMovieFragment(resultTrendingMovies.id)
        findNavController().navigate(action)
    }
}