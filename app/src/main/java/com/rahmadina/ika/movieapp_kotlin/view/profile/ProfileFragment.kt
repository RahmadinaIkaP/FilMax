package com.rahmadina.ika.movieapp_kotlin.view.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rahmadina.ika.movieapp_kotlin.R
import com.rahmadina.ika.movieapp_kotlin.data.datastore.SharedPref
import com.rahmadina.ika.movieapp_kotlin.data.network.ApiResponse
import com.rahmadina.ika.movieapp_kotlin.databinding.FragmentProfileBinding
import com.rahmadina.ika.movieapp_kotlin.utils.toast
import com.rahmadina.ika.movieapp_kotlin.view.auth.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val vmAuth : AuthViewModel by viewModels()
    private lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = SharedPref(requireContext())

        getUserInfo()

        binding.apply {
            navigateToFavorite.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_favoriteFragment)
            }

            btnLogout.setOnClickListener {
                logout()
                clearSession()
            }
        }

    }

    private fun getUserInfo() {
        vmAuth.getUser()
        vmAuth.getUserObserver().observe(viewLifecycleOwner){ response ->
            when(response){
                is ApiResponse.Error -> {
                    Log.e("LoginFragment","error get data")
                }
                is ApiResponse.Loading -> {
                    Log.d("LoginFragment", "loading...")
                }
                is ApiResponse.Success -> {
                    binding.apply {
                        tvNamaUser.text = response.data!!.first().name
                        tvEmailUser.text = response.data!!.first().email
                    }
                }
            }
        }
    }

    private fun logout() {
        vmAuth.logout {
            toast("Logout successfull!")
            findNavController().navigate(R.id.action_profileFragment_to_preLoginFragment)
        }
    }

    private fun clearSession() {
        CoroutineScope(Dispatchers.IO).launch {
            sharedPref.removeSession()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}