package com.mina.news.breakingnews.models.repository


data class Articles(

    var status: String? = null,
    var totalResults: Int? = null,
    var articles: List<Article>

)