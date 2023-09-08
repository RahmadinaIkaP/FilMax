package com.rahmadina.ika.movieapp_kotlin.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.rahmadina.ika.movieapp_kotlin.R
import com.rahmadina.ika.movieapp_kotlin.data.datastore.SharedPref
import com.rahmadina.ika.movieapp_kotlin.data.model.Favorite
import com.rahmadina.ika.movieapp_kotlin.data.network.ApiResponse
import com.rahmadina.ika.movieapp_kotlin.databinding.FragmentFavoriteBinding
import com.rahmadina.ika.movieapp_kotlin.utils.hide
import com.rahmadina.ika.movieapp_kotlin.utils.show
import com.rahmadina.ika.movieapp_kotlin.utils.toast
import com.rahmadina.ika.movieapp_kotlin.view.profile.adapter.FavoriteAdapter
import com.rahmadina.ika.movieapp_kotlin.view.profile.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(), FavoriteAdapter.FavoriteInterface {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val vmFav : FavoriteViewModel by viewModels()
    private lateinit var adapter: FavoriteAdapter
    private lateinit var sharedPref : SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = SharedPref(requireContext())

        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            getFavorite()
        }
    }

    private fun getFavorite() {
        sharedPref.getIdUser.asLiveData().observe(requireActivity()){ id ->
            if (id != "Undefined"){
                vmFav.getFavUser(id)

                vmFav.getFavUserObserver().observe(viewLifecycleOwner){ response ->
                    when(response){
                        is ApiResponse.Error -> {
                            toast(response.msg.toString())
                        }
                        is ApiResponse.Loading -> {}
                        is ApiResponse.Success -> {
                            if (response.data?.isEmpty() == true){
                                binding.imageView.show()
                                binding.lblempty.show()
                            }else{
                                binding.imageView.hide()
                                binding.lblempty.hide()
                                response.data?.let { showRvFav(it) }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showRvFav(data: List<Favorite>) {
        adapter = FavoriteAdapter(this)
        adapter.setData(data)

        binding.apply {
            rvFavoriteMovies.layoutManager =
                GridLayoutManager(context, 2)
            rvFavoriteMovies.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(favorite: Favorite) {
        if (favorite.type == "movie"){
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailMovieFragment(favorite.id.toInt())
            findNavController().navigate(action)
        }else if (favorite.type == "tv"){
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailTvShowFragment(favorite.id.toInt())
            findNavController().navigate(action)
        }
    }
}