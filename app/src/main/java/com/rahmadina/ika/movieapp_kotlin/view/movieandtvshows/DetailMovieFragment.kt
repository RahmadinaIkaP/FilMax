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
import com.rahmadina.ika.movieapp_kotlin.data.model.movie.ResponseDetailMovie
import com.rahmadina.ika.movieapp_kotlin.data.network.ApiResponse
import com.rahmadina.ika.movieapp_kotlin.databinding.FragmentDetailMovieBinding
import com.rahmadina.ika.movieapp_kotlin.utils.toast
import com.rahmadina.ika.movieapp_kotlin.view.home.adapter.GenreAdapter
import com.rahmadina.ika.movieapp_kotlin.view.movieandtvshows.viewmodel.MovieViewModel
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
class DetailMovieFragment : Fragment() {

    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() =  _binding!!
    private val vmMovie : MovieViewModel by viewModels()
    private val vmFav : FavoriteViewModel by viewModels()
    private val args : DetailMovieFragmentArgs by navArgs()
    private lateinit var adapter : GenreAdapter
    private var isClicked = false
    private lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = SharedPref(requireContext())
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
                                    }
                                    else{
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
                        checkButtonClicked(isClicked, Favorite(idUser = it, data.id.toString(), data.title, data.posterPath, "movie"))
                    }else{
                        findNavController().navigate(R.id.action_detailMovieFragment_to_preLoginFragment)
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