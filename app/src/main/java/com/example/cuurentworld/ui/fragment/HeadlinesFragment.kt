package com.example.cuurentworld.ui.fragment

import android.content.Context
import android.os.Bundle
import android.provider.SyncStateContract.Constants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cuurentworld.R
import com.example.cuurentworld.databinding.FragmentHeadlinesBinding
import com.example.cuurentworld.ui.NewsActivity
import com.example.cuurentworld.ui.adapter.NewsAdapter
import com.example.cuurentworld.ui.viewmodel.NewsViewModel
import com.example.cuurentworld.util.Resource
import retrofit2.Response


class HeadlinesFragment : Fragment(R.layout.fragment_headlines) {

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var binding: FragmentHeadlinesBinding
    lateinit var newsAdapter: NewsAdapter

    lateinit var retryButton:Button
    lateinit var errorText:TextView
    lateinit var itemHeadlinesError:CardView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentHeadlinesBinding.bind(view)



        itemHeadlinesError = view.findViewById(R.id.itemHeadlinesError)

        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val  view:View = inflater.inflate(R.layout.item_error,null)
        retryButton = view.findViewById(R.id.retryButton)
        errorText =view.findViewById(R.id.errorText)
        newsViewModel=(activity as NewsActivity).newsViewModel
        setupHeadlinesRecycler()

        newsAdapter.setOnClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)

            }
            findNavController().navigate(R.id.action_headlinesFragment2_to_articleFragment,bundle)
        }

        newsViewModel.headlines.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success<*> -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages =
                            newsResponse.totalResults / com.example.cuurentworld.util.Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = newsViewModel.headlinesPage == totalPages
                        if (isLastPage) {
                            binding.recyclerHeadlines.setPadding(0,0,0,0)

                        }
                    }
                }
                is Resource.Error<*> ->{

                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity,"Sorry error : $message",Toast.LENGTH_SHORT).show()
                        showErrorMessage(message)
                    }
                }
                is Resource.Loading<*> ->{
                    showProgressBar()

                }
            }
        })
        retryButton.setOnClickListener {
            newsViewModel.getHeadlines("us")
        }

    }

    var isError = false
    var isLoading =false
    var isLastPage = false
    var isScrolling = false

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility =View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar(){
        binding.paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
    }

    private fun hideErrorMessage(){
        itemHeadlinesError.visibility = View.INVISIBLE
        isError = false
    }
    private fun showErrorMessage(message:String){
        itemHeadlinesError.visibility = View.INVISIBLE
        errorText.text=message
        isError = true
    }


    val scrollListener = object :RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible =
                totalItemCount >= com.example.cuurentworld.util.Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                newsViewModel.getHeadlines("us")
                isScrolling = false

            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                isScrolling = true
            }
        }
    }

        private fun setupHeadlinesRecycler(){
            newsAdapter = NewsAdapter()
            binding.recyclerHeadlines.apply {
                adapter = newsAdapter
                layoutManager = LinearLayoutManager(activity)
                addOnScrollListener(this@HeadlinesFragment.scrollListener)
            }
        }
    }

