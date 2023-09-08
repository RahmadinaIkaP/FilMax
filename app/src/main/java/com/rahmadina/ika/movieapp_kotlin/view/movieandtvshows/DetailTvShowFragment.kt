package com.rahmadina.ika.movieapp_kotlin.view.movieandtvshows

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.rahmadina.ika.movieapp_kotlin.R
import com.rahmadina.ika.movieapp_kotlin.data.datastore.SharedPref
import com.rahmadina.ika.movieapp_kotlin.data.model.Favorite
import com.rahmadina.ika.movieapp_kotlin.data.model.movie.Genre
import com.rahmadina.ika.movieapp_kotlin.data.model.tvshows.ResponseDetailTvShows
import com.rahmadina.ika.movieapp_kotlin.data.network.ApiResponse
import com.rahmadina.ika.movieapp_kotlin.databinding.FragmentDetailTvShowBinding
import com.rahmadina.ika.movieapp_kotlin.utils.toast
import com.rahmadina.ika.movieapp_kotlin.view.home.adapter.GenreAdapter
import com.rahmadina.ika.movieapp_kotlin.view.movieandtvshows.viewmodel.TvShowsViewModel
import com.rahmadina.ika.movieapp_kotlin.view.profile.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class DetailTvShowFragment : Fragment() {

    private var _binding: FragmentDetailTvShowBinding? = null
    private val binding get() = _binding!!
    private val vmTvShows : TvShowsViewModel by viewModels()
    private val vmFav : FavoriteViewModel by viewModels()
    private val args : DetailTvShowFragmentArgs by navArgs()
    private lateinit var adapter : GenreAdapter
    private var isClicked = false
    private lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailTvShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = SharedPref(requireContext())
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

            GlobalScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main){
                    val count = vmFav.checkFav(data.id.toString())

                    vmFav.checkFavObserver().observe(viewLifecycleOwner){response ->
                        when(response){
                            is ApiResponse.Success -> {
                                sharedPref.getIdUser.asLiveData().observe(viewLifecycleOwner){
                                    if (it != "Undefined"){
                                        isClicked = if (response.data!!.isNotEmpty()){
                                            binding.imgAddFav.setImageResource(R.drawable.baseline_check_24)
                                            binding.tvAddFav.text = "Favorited!"
                                            true
                                        }else{
                                            binding.imgAddFav.setImageResource(R.drawable.baseline_add_24)
                                            binding.tvAddFav.text = "Add to Favorite"
                                            false
                                        }
                                    }else{
                                        binding.imgAddFav.setImageResource(R.drawable.baseline_add_24)
                                        binding.tvAddFav.text = "Add to Favorite"
                                    }
                                }
                            }
                            is ApiResponse.Loading -> {

                            }
                            is ApiResponse.Error -> {
                                toast(response.msg.toString())
                            }
                        }
                    }

                }
            }

            btnAddToFav.setOnClickListener {
                sharedPref.getIdUser.asLiveData().observe(viewLifecycleOwner){
                    if (it != "Undefined"){
                        isClicked = !isClicked
                        checkButtonClicked(isClicked, Favorite(idUser = it, data.id.toString(), data.name, data.posterPath, "tv"))
                    }else{
                        findNavController().navigate(R.id.action_detailTvShowFragment_to_preLoginFragment)
                    }
                }
            }
        }
    }

    private fun checkButtonClicked(clicked: Boolean, favorite: Favorite) {
        if (isClicked){
            vmFav.addToFav(favorite)
            vmFav.addFavObserver().observe(viewLifecycleOwner){ response ->
                when(response){
                    is ApiResponse.Error -> {
                        toast(response.msg.toString())
                    }
                    is ApiResponse.Loading -> {

                    }
                    is ApiResponse.Success -> {
                        binding.imgAddFav.setImageResource(R.drawable.baseline_check_24)
                        toast("Add to Favorit Successfull!")
                    }
                }
            }
        }
        else{
            vmFav.removeFav(favorite)
            vmFav.removeFavObserver().observe(viewLifecycleOwner){ response ->
                when(response){
                    is ApiResponse.Error -> {
                        toast(response.msg.toString())
                    }
                    is ApiResponse.Loading -> {

                    }
                    is ApiResponse.Success -> {
                        binding.imgAddFav.setImageResource(R.drawable.baseline_add_24)
                        toast("Remove from favorite")
                    }
                }
            }
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