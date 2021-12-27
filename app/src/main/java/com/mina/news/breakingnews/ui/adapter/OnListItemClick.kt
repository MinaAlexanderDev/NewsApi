package com.mina.news.breakingnews.ui.adapter

import com.mina.news.breakingnews.models.repository.Article


interface OnListItemClick {

    fun onItemFavorite(article: Article)
    fun onItemUnFavorite(article: Article)
    fun onItemSelect(article: Article)
}