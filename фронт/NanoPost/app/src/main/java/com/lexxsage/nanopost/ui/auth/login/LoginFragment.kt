package com.lexxsage.nanopost.ui.auth.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.lexxsage.nanopost.R
import com.lexxsage.nanopost.databinding.FragmentLoginBinding
import com.lexxsage.nanopost.ui.BaseFragment
import com.lexxsage.nanopost.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    override val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleScope.launchWhenCreated {
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

    override fun showError(e: Exception) {
        super.showError(e)
        binding.loginButton.isEnabled = true
    }
}
