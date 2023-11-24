package com.example.cuurentworld.models

import com.example.cuurentworld.models.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)

// List -> MutableList