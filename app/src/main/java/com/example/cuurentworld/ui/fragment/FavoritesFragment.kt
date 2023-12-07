package com.example.cuurentworld.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cuurentworld.R
import com.example.cuurentworld.databinding.FragmentFavoritesBinding
import com.example.cuurentworld.ui.NewsActivity
import com.example.cuurentworld.ui.adapter.NewsAdapter
import com.example.cuurentworld.ui.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class FavoritesFragment : Fragment() {

 lateinit var newsViewModel: NewsViewModel
 lateinit var newsAdapter: NewsAdapter
 lateinit var binding: FragmentFavoritesBinding


 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)
  binding = FragmentFavoritesBinding.bind(view)


  newsViewModel = (activity as NewsActivity).newsViewModel
  setupFavoritesRecycler()

  newsAdapter.setOnClickListener {
   val bundle = Bundle().apply {
    putSerializable("article", it)

   }
   findNavController().navigate(R.id.action_favoritesFragment2_to_articleFragment, bundle)
  }

  val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
   ItemTouchHelper.UP or ItemTouchHelper.DOWN,
   ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
  ) {


   override fun onMove(
    recyclerView: RecyclerView,
    viewHolder: RecyclerView.ViewHolder,
    target: RecyclerView.ViewHolder
   ): Boolean {
    return true
   }

   override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
    val position = viewHolder.adapterPosition
    val article = newsAdapter.differ.currentList[position]
    newsViewModel.deleteArticle(article)
    Snackbar.make(view, "Removed from favorities", Snackbar.LENGTH_LONG).apply {
     setAction("Undo") {
      newsViewModel.addToFavorites(article)
     }
     show()
    }
   }
  }
  ItemTouchHelper(itemTouchHelperCallback).apply {
   attachToRecyclerView(binding.recyclerFavourites)
  }


 }

 private fun setupFavoritesRecycler(){
  newsAdapter = NewsAdapter()
  binding.recyclerFavourites.apply {
   adapter = newsAdapter
   layoutManager = LinearLayoutManager(activity)

  }
 }
}