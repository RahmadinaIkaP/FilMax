package com.rahmadina.ika.movieapp_kotlin.view.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rahmadina.ika.movieapp_kotlin.R
import com.rahmadina.ika.movieapp_kotlin.data.model.User
import com.rahmadina.ika.movieapp_kotlin.data.network.ApiResponse
import com.rahmadina.ika.movieapp_kotlin.databinding.FragmentRegisterBinding
import com.rahmadina.ika.movieapp_kotlin.utils.isValidEmail
import com.rahmadina.ika.movieapp_kotlin.utils.toast
import com.rahmadina.ika.movieapp_kotlin.view.auth.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val vmAuth : AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            btnDaftar.setOnClickListener {
                registAccount()
            }

            btnToLogin.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    private fun registAccount() {
        binding.apply {
            if (validaton()){
                vmAuth.register(
                    etEmailReg.text.toString(),
                    etPasswordReg.text.toString(),
                    User(
                        id = "",
                        email = etEmailReg.text.toString(),
                        name = etNameReg.text.toString()
                    )
                )

                observeRegister()
            }
        }
    }

    private fun observeRegister() {
        vmAuth.registerObserver().observe(viewLifecycleOwner){ response ->
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
                    toast(response.data.toString())
                    if (findNavController().currentDestination?.id != R.id.loginFragment) {
                        findNavController().navigate(
                            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                        )
                    }
                }
            }
        }
    }

    private fun validaton(): Boolean {
        var isValid = true

        binding.apply {
            if (etEmailReg.text.isNullOrEmpty()){
                isValid = false
                toast("Email can't be empty!")

                if (!etEmailReg.text.toString().isValidEmail()){
                    isValid = false
                    toast("Email is not valid!")
                }
            }else if (etNameReg.text.isNullOrEmpty()){
                isValid = false
                toast("Name can't be empty!")

            }else if (etPasswordReg.text.isNullOrEmpty() || etKonfirmPassReg.text.isNullOrEmpty()){
                isValid = false
                toast("Password can't be empty!")

                if (etPasswordReg.text.toString().length < 8){
                    isValid = false
                    toast("Password must have minimum 8 characters!")

                    if (etKonfirmPassReg != etPasswordReg){
                        isValid = false
                        toast("Password doesn't match!")
                    }
                }
            }
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}