package com.mina.news.breakingnews.ui.search

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
import com.mina.news.R
import com.mina.news.breakingnews.models.repository.Article
import com.mina.news.breakingnews.ui.adapter.ItemLoadStateAdapter
import com.mina.news.breakingnews.ui.adapter.ItemsRecyclerViewAdapter
import com.mina.news.breakingnews.ui.adapter.OnListItemClick
import com.mina.news.breakingnews.ui.viewmodel.NewsViewModel
import com.mina.news.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search), OnListItemClick {

    private val viewModel by viewModels<NewsViewModel>()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val adapter = ItemsRecyclerViewAdapter(this)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSearchBinding.bind(view)


        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            val searchQuery = savedInstanceState.getString(LAST_SEARCH_QUERY)

            lifecycleScope.launch {
                when {
                    searchQuery != null -> {

                        viewModel.getSearchNews(searchQuery).collectLatest {
                            adapter.submitData(it)
                            adapter.notifyDataSetChanged()

                        }

                    }
                    _binding != null -> {
                        viewModel.getSearchNews(binding.etSearch.text.toString()).collectLatest {
                            adapter.submitData(it)
                            adapter.notifyDataSetChanged()

                        }
                    }
                    else -> {

                    }
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
        adapter.addLoadStateListener { loadState ->

            if (_binding != null) {
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

        }

        binding.btuSearch.setOnClickListener {
            lifecycleScope.launch {
                viewModel.getSearchNews(binding.etSearch.text.toString()).collect {

                    adapter.submitData(it)
                    adapter.notifyDataSetChanged()
                }
            }
            binding.recyclerView.scrollToPosition(0)
        }
        setHasOptionsMenu(true)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        if (_binding != null) {
            savedInstanceState.putString(LAST_SEARCH_QUERY, binding.etSearch.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemFavorite(article: Article) {
        viewModel.addArticleToFavorite(article)
        Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show()
    }

    override fun onItemUnFavorite(article: Article) {
        Log.e(TAG, "onItemUnFavorite $article")
    }

    override fun onItemSelect(article: Article) {
        Log.e(TAG, "onItemSelect $article")
        val action = SearchFragmentDirections.actionSearchToDetailsFragment(article)
        findNavController().navigate(action)
    }

    companion object {
        const val TAG: String = "SearchFragment"
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
    }

}