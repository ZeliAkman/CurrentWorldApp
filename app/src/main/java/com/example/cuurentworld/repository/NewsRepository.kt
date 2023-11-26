package com.example.cuurentworld.repository

import com.example.cuurentworld.api.RetrofitInstance
import com.example.cuurentworld.db.ArticleDatabase
import com.example.cuurentworld.models.Article
import com.example.cuurentworld.db.ArticleDAO


class NewsRepository(val db:ArticleDatabase) {

    suspend fun getHeadlines(countryCode: String,pageNumber : Int ) =
     RetrofitInstance.api.getHeadlines(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String,pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) =
        db.getArticleDao().upsert(article)

    fun getFavoriteNews()= db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}