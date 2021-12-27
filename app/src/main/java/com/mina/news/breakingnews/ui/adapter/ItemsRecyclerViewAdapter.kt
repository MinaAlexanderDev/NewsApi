package com.mina.news.breakingnews.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mina.news.R
import com.mina.news.breakingnews.models.repository.Article
import com.mina.news.databinding.ListItemRepositoryBinding
import kotlinx.android.synthetic.main.list_item_repository.view.*


class ItemsRecyclerViewAdapter(private val listener: OnListItemClick) :
    PagingDataAdapter<Article, ItemsRecyclerViewAdapter.ImageViewHolder>(diffCallback) {


    inner class ImageViewHolder(binding: ListItemRepositoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var imgAvatar: ImageView = itemView.findViewById(R.id.img_avatar)
        private var tvUserName: TextView = itemView.findViewById(R.id.tv_item_userName)
        private var tvMessage: TextView = itemView.findViewById(R.id.tv_item_title)

        init {

            binding.root.iv_item_favorite.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemFavorite(item)
                    }
                }
            }
        }

        fun bind(user: Article) {
            tvUserName.text = user.title
            tvMessage.text = user.description


            Glide.with(imgAvatar)
                .load(user.urlToImage)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(imgAvatar)

            imgAvatar.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemSelect(item)
                    }
                }
            }
        }
    }


    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.source?.id == oldItem.source?.id
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.source?.id == oldItem.source?.id
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        return ImageViewHolder(
            ListItemRepositoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currChar = getItem(position)
        if (currChar != null) {
            holder.bind(currChar)
        }
    }

}