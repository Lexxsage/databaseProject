package com.lexxsage.nanopost.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lexxsage.nanopost.databinding.ItemLoaderBinding

class StateAdapter(
    private val retry: () -> Unit,
) : LoadStateAdapter<StateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) = ViewHolder(
        ItemLoaderBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        retry,
    )

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class ViewHolder(
        private val binding: ItemLoaderBinding,
        retry: () -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMessage.text = loadState.error.localizedMessage
            }
            binding.spinner.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMessage.isVisible = loadState is LoadState.Error
        }
    }
}
