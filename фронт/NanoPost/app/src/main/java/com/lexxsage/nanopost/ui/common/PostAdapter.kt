package com.lexxsage.nanopost.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lexxsage.nanopost.databinding.ItemPostBinding
import com.lexxsage.nanopost.network.model.PostApiModel
import java.text.SimpleDateFormat

class PostAdapter : PagingDataAdapter<PostApiModel, PostAdapter.PostViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }

    class PostViewHolder(
        private val binding: ItemPostBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: PostApiModel) {
            data.owner.avatar?.url?.let {
                binding.ownerAvatar.load(it)
            } ?: binding.ownerAvatar.setImageDrawable(null)
            binding.ownerName.text = data.owner.displayName ?: data.owner.username
            binding.date.text = SimpleDateFormat.getDateTimeInstance().format(data.dateCreated)

            binding.text.apply {
                text = data.text
                isVisible = !data.text.isNullOrBlank()
            }
            binding.image.apply {
                data.image?.url?.let { load(it) }
                isVisible = !data.image?.url.isNullOrBlank()
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<PostApiModel>() {
        override fun areItemsTheSame(oldItem: PostApiModel, newItem: PostApiModel): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: PostApiModel, newItem: PostApiModel): Boolean {
            return newItem == oldItem
        }
    }
}
