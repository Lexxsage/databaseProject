package com.lexxsage.nanopost.ui.auth.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lexxsage.nanopost.R
import com.lexxsage.nanopost.databinding.FragmentRegisterBinding
import com.lexxsage.nanopost.focus
import com.lexxsage.nanopost.textChanges
import com.lexxsage.nanopost.ui.BaseFragment
import com.lexxsage.nanopost.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RegisterFragment : BaseFragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)
    override val viewModel by viewModels<RegisterViewModel>()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleScope.launchWhenCreated {
            viewModel.result.collect {
                if (it) {
                    findNavController().popBackStack(R.id.auth_fragment, true)
                }
            }

            binding.usernameField.editText!!.textChanges()
                .debounce(500L)
                .onEach {
                    setUsernameFieldState(UsernameState.Loading)
                }
                .mapLatest { viewModel.checkUsername(it.toString()) }
                .collect {
                    setUsernameFieldState(
                        when (it) {
                            true -> UsernameState.Available
                            false -> UsernameState.Taken
                            null -> UsernameState.None
                        }
                    )
                }
        }

        with(binding) {
            registerButton.setOnClickListener {
                viewModel.register(
                    usernameField.editText!!.text.toString(),
                    passwordField.editText!!.text.toString(),
                )
                registerButton.isEnabled = false
            }
        }
    }

    override fun showError(e: Exception) {
        super.showError(e)
        binding.registerButton.isEnabled = true
    }

    private fun setUsernameFieldState(state: UsernameState) {
        binding.usernameField.apply {
            when (state) {
                UsernameState.Available -> {
                    setEndIconDrawable(R.drawable.ic_check_24)
                    helperText = "This username is available"
                    error = null
                }
                UsernameState.Taken -> {
                    setEndIconDrawable(R.drawable.ic_cross_24)
                    helperText = null
                    error = "Sorry, this username is already taken"
                }
                UsernameState.Loading -> {
                    endIconDrawable = null
                    helperText = "Checking availability..."
                    error = null
                }
                UsernameState.None -> {
                    endIconDrawable = null
                    helperText = null
                    error = null
                }
            }
        }
    }
}
