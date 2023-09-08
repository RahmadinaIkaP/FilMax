package com.rahmadina.ika.movieapp_kotlin.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.rahmadina.ika.movieapp_kotlin.R
import com.rahmadina.ika.movieapp_kotlin.data.datastore.SharedPref
import com.rahmadina.ika.movieapp_kotlin.databinding.FragmentPreLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreLoginFragment : Fragment() {

    private var _binding: FragmentPreLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPref : SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = SharedPref(requireContext())

        checkUser()
        binding.apply {
            btnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_preLoginFragment_to_loginFragment)
            }

            btnRegister.setOnClickListener {
                findNavController().navigate(R.id.action_preLoginFragment_to_registerFragment)
            }
        }
    }

    private fun checkUser() {
        sharedPref.getSession.asLiveData().observe(viewLifecycleOwner){
            if (it != false){
                findNavController().navigate(R.id.action_preLoginFragment_to_profileFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}