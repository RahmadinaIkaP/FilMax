package com.rahmadina.ika.movieapp_kotlin.view.home.search

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.rahmadina.ika.movieapp_kotlin.data.model.search.ResultSearch
import com.rahmadina.ika.movieapp_kotlin.data.network.ApiResponse
import com.rahmadina.ika.movieapp_kotlin.databinding.FragmentSearchResultBinding
import com.rahmadina.ika.movieapp_kotlin.utils.toast
import com.rahmadina.ika.movieapp_kotlin.view.home.adapter.SearchAdapter
import com.rahmadina.ika.movieapp_kotlin.view.movieandtvshows.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultFragment : Fragment(), SearchAdapter.SearchResultInterface {
    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!
    private val vmMovie : MovieViewModel by viewModels()
    private lateinit var adapter : SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        val data = arguments?.getString("keyword")

        binding.tvKeyword.text = "Result for: $data"
        data?.let { setResultSearch(it) }
    }

    private fun setResultSearch(data: String) {
        vmMovie.getSearchResult(data)
        vmMovie.getSearchObserver().observe(viewLifecycleOwner){ response ->
            when(response){
                is ApiResponse.Success -> {
                    binding.progressBar.hide()
                    response.data?.let { showRvSearchResult(it.results) }
                }
                is ApiResponse.Loading -> {
                    binding.progressBar.show()
                }
                is ApiResponse.Error -> {
                    binding.progressBar.hide()
                    toast(response.msg.toString())
                }
            }
        }
    }

    private fun showRvSearchResult(data: List<ResultSearch>) {
        val filteredData = data.filter { it.mediaType == "movie" || it.mediaType == "tv"}

        adapter = SearchAdapter(this)
        adapter.setData(filteredData)

        binding.apply {
            rvPopularMovies.layoutManager =
                GridLayoutManager(requireContext(), 2)
            rvPopularMovies.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(resultSearch: ResultSearch) {
        if (resultSearch.mediaType == "movie"){
            val action =
                SearchResultFragmentDirections.actionSearchResultFragmentToDetailMovieFragment(
                    resultSearch.id
                )
            findNavController().navigate(action)
        } else if (resultSearch.mediaType == "tv"){
            val action =
                SearchResultFragmentDirections.actionSearchResultFragmentToDetailTvShowFragment(
                    resultSearch.id
                )
            findNavController().navigate(action)
        }
    }
}