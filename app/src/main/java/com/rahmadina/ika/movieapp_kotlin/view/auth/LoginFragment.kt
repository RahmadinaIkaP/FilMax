package com.rahmadina.ika.movieapp_kotlin.view.auth

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
import com.rahmadina.ika.movieapp_kotlin.databinding.FragmentLoginBinding
import com.rahmadina.ika.movieapp_kotlin.utils.toast
import com.rahmadina.ika.movieapp_kotlin.view.auth.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val vmAuth : AuthViewModel by viewModels()
    private lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = SharedPref(requireContext())

        binding.apply {
            btnToRegister.setOnClickListener {
                findNavController().navigate(R.id.action_preLoginFragment_to_registerFragment)
            }

            btnLogin.setOnClickListener {
                processLogin()
            }
        }
    }

    private fun processLogin() {
        binding.apply {
            if(validation()){
                vmAuth.login(etEmailLogin.text.toString(), etPasswordLogin.text.toString())
                observeLogin()
            }
        }
    }

    private fun observeLogin() {
        vmAuth.loginObserver().observe(viewLifecycleOwner){ response ->
            when(response){
                is ApiResponse.Error -> {
                    binding.progressBar.hide()
                    toast(response.msg.toString())
                }
                is ApiResponse.Loading -> {
                    binding.progressBar.show()
                }
                is ApiResponse.Success -> {
                    binding.progressBar.hide()
                    getUser()
                    toast(response.data.toString())
                }
            }
        }
    }

    private fun getUser() {
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
                    response.data?.get(0)?.let { Log.d("LoginFragment", it.toString()) }
                    saveSession(response.data?.first()!!.id, response.data.first().email, response.data.first().name)
                    findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
                }
            }
        }
    }

    private fun saveSession(id: String, email: String, name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            sharedPref.saveUser(true, id)
        }
    }

    private fun validation(): Boolean {
        var isValid = true

        binding.apply {
            if (etEmailLogin.text.isNullOrEmpty()){
                isValid = false
                toast("Email kosong, silahkan isi terlebih dahulu")
            }else if (etPasswordLogin.text.isNullOrEmpty()){
                isValid = false
                toast("Password kosong, silahkan isi terlebih dahulu")
            }
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}