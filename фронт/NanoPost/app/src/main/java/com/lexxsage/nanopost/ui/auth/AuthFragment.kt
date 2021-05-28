package com.lexxsage.nanopost.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lexxsage.nanopost.R
import com.lexxsage.nanopost.databinding.FragmentAuthBinding
import com.lexxsage.nanopost.repository.SettingsRepository
import com.lexxsage.nanopost.ui.BaseFragment
import com.lexxsage.nanopost.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : BaseFragment(R.layout.fragment_auth) {

    private val binding by viewBinding(FragmentAuthBinding::bind)

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_auth_fragment_to_login_fragment)
        }
        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_auth_fragment_to_register_fragment)
        }

        settingsRepository.authToken?.let {
            findNavController().navigate(R.id.feed_fragment)
        }
    }
}
