package com.lexxsage.nanopost.ui.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.lexxsage.nanopost.R
import com.lexxsage.nanopost.databinding.FragmentFeedBinding
import com.lexxsage.nanopost.ui.BaseFragment
import com.lexxsage.nanopost.ui.common.PostAdapter
import com.lexxsage.nanopost.ui.common.StateAdapter
import com.lexxsage.nanopost.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FeedFragment : BaseFragment(R.layout.fragment_feed) {

    private val binding by viewBinding(FragmentFeedBinding::bind)
    override val viewModel by viewModels<FeedViewModel>()

    private lateinit var itemAdapter: PostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemAdapter = PostAdapter()
        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ConcatAdapter(
                PostCreateAdapter {

                },
                itemAdapter.withLoadStateFooter(StateAdapter(itemAdapter::retry)),
            )
        }

        viewLifecycleScope.launchWhenCreated {
            viewModel.pager.collectLatest(itemAdapter::submitData)
        }
    }
}
