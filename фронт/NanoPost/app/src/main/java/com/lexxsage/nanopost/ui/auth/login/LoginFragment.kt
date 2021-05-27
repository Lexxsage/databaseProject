package com.lexxsage.nanopost.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.lexxsage.nanopost.R
import com.lexxsage.nanopost.databinding.FragmentLoginBinding
import com.lexxsage.nanopost.ui.BaseFragment
import com.lexxsage.nanopost.ui.auth.AuthViewModel
import com.lexxsage.nanopost.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    override val viewModel by viewModels<AuthViewModel>()
    private var errorMessage: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.errors().collect { e ->
                errorMessage?.dismiss()
                errorMessage = Snackbar.make(
                    binding.root,
                    e.localizedMessage ?: "An error occurred",
                    Snackbar.LENGTH_SHORT,
                ).also { it.show() }
                binding.loginButton.isEnabled = true
            }

            viewModel.result.collect {
                if (it) {
                    findNavController().popBackStack(R.id.auth_fragment, true)
                }
            }
        }

        with(binding) {
            loginButton.setOnClickListener {
                viewModel.login(
                    usernameField.editText!!.text.toString(),
                    passwordField.editText!!.text.toString(),
                )
                loginButton.isEnabled = false
            }
        }
    }
}
