package com.lexxsage.nanopost.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.lexxsage.nanopost.R
import com.lexxsage.nanopost.databinding.FragmentProfileBinding
import com.lexxsage.nanopost.ui.BaseFragment
import com.lexxsage.nanopost.ui.common.PostAdapter
import com.lexxsage.nanopost.ui.common.StateAdapter
import com.lexxsage.nanopost.ui.feed.PostCreateAdapter
import com.lexxsage.nanopost.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val arguments by navArgs<ProfileFragmentArgs>()
    override val viewModel by viewModels<ProfileViewModel>()

    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var postAdapter: PostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileAdapter = ProfileAdapter {

        }
        postAdapter = PostAdapter()
        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ConcatAdapter(
                profileAdapter,
                postAdapter.withLoadStateFooter(StateAdapter(postAdapter::retry)),
            )
        }

        viewLifecycleScope.launchWhenCreated {
            viewModel.pager.collectLatest(postAdapter::submitData)
            profileAdapter.data = viewModel.getProfile(arguments.id)
        }
    }
}
