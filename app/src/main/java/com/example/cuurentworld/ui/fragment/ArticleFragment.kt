package com.example.cuurentworld.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.cuurentworld.R
import com.example.cuurentworld.databinding.FragmentArticleBinding
import com.example.cuurentworld.ui.NewsActivity
import com.example.cuurentworld.ui.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class ArticleFragment : Fragment(R.layout.fragment_article) {
    private lateinit var binding: FragmentArticleBinding
    lateinit var newsViewModel: NewsViewModel
    val args:ArticleFragmentArgs by  navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)




        newsViewModel = (activity as NewsActivity).newsViewModel
        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let {
                loadUrl(it)
            }
        }
        binding.fab.setOnClickListener {
            newsViewModel.addToFavorites(article)
            Snackbar.make(view,"Added to favorites ",Snackbar.LENGTH_SHORT).show()

        }

    }


}