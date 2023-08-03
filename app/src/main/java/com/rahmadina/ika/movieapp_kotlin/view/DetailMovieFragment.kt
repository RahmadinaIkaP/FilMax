package com.rahmadina.ika.movieapp_kotlin.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.rahmadina.ika.movieapp_kotlin.data.model.movie.Genre
import com.rahmadina.ika.movieapp_kotlin.data.model.movie.ResponseDetailMovie
import com.rahmadina.ika.movieapp_kotlin.data.network.ApiResponse
import com.rahmadina.ika.movieapp_kotlin.databinding.FragmentDetailMovieBinding
import com.rahmadina.ika.movieapp_kotlin.utils.toast
import com.rahmadina.ika.movieapp_kotlin.view.adapter.GenreAdapter
import com.rahmadina.ika.movieapp_kotlin.view.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class DetailMovieFragment : Fragment() {

    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() =  _binding!!
    private val vmMovie : MovieViewModel by viewModels()
    private val args : DetailMovieFragmentArgs by navArgs()
    private lateinit var adapter : GenreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = args.argId

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        setDetailMovie(data)
    }

    private fun setDetailMovie(id: Int) {
        vmMovie.getDetailMovie(id)
        vmMovie.getDetailMovieObserver().observe(viewLifecycleOwner){response ->
            when(response){
                is ApiResponse.Success -> {
                    response.data?.let { showDetail(it) }
                }
                is ApiResponse.Loading -> {

                }
                is ApiResponse.Error -> {
                    toast(response.msg.toString())
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDetail(data: ResponseDetailMovie) {

        binding.apply {
            val df = DecimalFormat("#.#")
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val movieYear = SimpleDateFormat("yyyy",
                Locale.getDefault()).format(dateFormat.parse(data.releaseDate)!!)

            Glide.with(requireContext())
                .load("https://image.tmdb.org/t/p/w400${data.posterPath}")
                .into(ivMovie)

            setRvGenre(data.genres)

            tvMovieName.text = data.title + " " + "(${movieYear})"
            tvRate.text = df.format(data.voteAverage)
            tvOverview.text = data.overview
        }
    }

    private fun setRvGenre(genres: List<Genre>) {
        adapter = GenreAdapter(genres)

        binding.apply {
            rvGenre.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvGenre.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}