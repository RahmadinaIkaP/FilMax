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
import com.rahmadina.ika.movieapp_kotlin.data.model.tvshows.ResponseDetailTvShows
import com.rahmadina.ika.movieapp_kotlin.data.network.ApiResponse
import com.rahmadina.ika.movieapp_kotlin.databinding.FragmentDetailTvShowBinding
import com.rahmadina.ika.movieapp_kotlin.utils.toast
import com.rahmadina.ika.movieapp_kotlin.view.adapter.GenreAdapter
import com.rahmadina.ika.movieapp_kotlin.view.viewmodel.TvShowsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class DetailTvShowFragment : Fragment() {

    private var _binding: FragmentDetailTvShowBinding? = null
    private val binding get() = _binding!!
    private val vmTvShows : TvShowsViewModel by viewModels()
    private val args : DetailTvShowFragmentArgs by navArgs()
    private lateinit var adapter : GenreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailTvShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = args.argId

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        setDetailTvShow(data)
    }

    private fun setDetailTvShow(id: Int) {
        vmTvShows.getDetailTvShows(id)
        vmTvShows.getDetailTvObserver().observe(viewLifecycleOwner){ response ->
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
    private fun showDetail(data: ResponseDetailTvShows) {
        val df = DecimalFormat("#.#")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val tvYear = SimpleDateFormat("yyyy",
            Locale.getDefault()).format(dateFormat.parse(data.firstAirDate)!!)

        binding.apply {
            Glide.with(requireContext())
                .load("https://image.tmdb.org/t/p/w400${data.posterPath}")
                .into(ivTvShows)

            setRvGenre(data.genres)

            tvShowName.text = data.name + " " + "(${tvYear})"
            tvRate.text = df.format(data.voteAverage)
            tvOverview.text = data.overview
        }
    }

    private fun setRvGenre(genres: List<Genre>) {
        adapter = GenreAdapter(genres)

        binding.apply {
            rvGenreTv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvGenreTv.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}