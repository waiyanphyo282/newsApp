package com.waiyanphyo.betternews.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.waiyanphyo.betternews.data.model.Article
import com.waiyanphyo.betternews.databinding.ItemArticleBinding

class ArticleAdapter: PagingDataAdapter<Article, ArticleAdapter.ArticleViewHolder>(ArticleDiff) {

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    object ArticleDiff: DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    inner class ArticleViewHolder(val binding: ItemArticleBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindData(article: Article) {
            binding.apply {
                ivArticle.load(article.urlToImage) {
                    crossfade(true)
                    transformations(RoundedCornersTransformation())
                }
                tvTitle.text = article.title
                tvDescription.text = article.description
                root.setOnClickListener {
                    onItemClickListener?.invoke(article)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        getItem(position)?.let { holder.bindData(it) }
    }
}