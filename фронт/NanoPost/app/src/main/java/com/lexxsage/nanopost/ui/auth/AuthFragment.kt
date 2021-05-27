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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

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
