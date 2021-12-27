package com.mina.news.breakingnews.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mina.news.R
import com.mina.news.breakingnews.models.repository.Article
import com.mina.news.breakingnews.ui.adapter.ItemLoadStateAdapter
import com.mina.news.breakingnews.ui.adapter.ItemsRecyclerViewAdapterFavorite
import com.mina.news.breakingnews.ui.adapter.OnListItemClick
import com.mina.news.breakingnews.ui.search.SearchFragment
import com.mina.news.breakingnews.ui.viewmodel.NewsViewModel
import com.mina.news.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite), OnListItemClick {

    private val viewModel by viewModels<NewsViewModel>()

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentFavoriteBinding.bind(view)

        val adapter = ItemsRecyclerViewAdapterFavorite(this)
        if (savedInstanceState != null) {
            // Restore value of members from saved state

            lifecycleScope.launch {
                viewModel.item.collect {
                    adapter.submitData(it)
                    adapter.notifyDataSetChanged()
                }

            }
        }
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = ItemLoadStateAdapter { adapter.retry() },
                footer = ItemLoadStateAdapter { adapter.retry() }
            )
            buttonRetry.setOnClickListener { adapter.retry() }
        }

        lifecycleScope.launch {
            viewModel.item.collect {
                adapter.submitData(it)
                adapter.notifyDataSetChanged()
            }
        }


        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error
                // empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }
        }
        setHasOptionsMenu(true)

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

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = adapter.getNoteAtPosition(position)
                if (article != null) {
                    viewModel.deleteArticleFromFavorite(article)
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.item.collect {
                            lifecycleScope.launch(Dispatchers.Main) {
                                adapter.submitData(it)
                                adapter.notifyDataSetChanged()
                            }

                        }
                    }
                }
                Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        article?.let { it -> viewModel.addArticleToFavorite(it) }
                        adapter.notifyDataSetChanged()
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerView)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemFavorite(article: Article) {
        Log.e(TAG, "onItemFavorite $article")
    }

    override fun onItemUnFavorite(article: Article) {
        Log.e(TAG, "onItemUnFavorite $article")
        viewModel.deleteArticleFromFavorite(article)
        Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show()
    }

    override fun onItemSelect(article: Article) {
        Log.e(TAG, "onItemSelect $article")
        val action = FavoriteFragmentDirections.actionFavoriteToDetailsFragment(article)
        findNavController().navigate(action)
    }

    companion object {
        const val TAG: String = "FavoriteFragment"
    }
}