package com.mina.news.breakingnews.ui.news

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
import com.mina.news.databinding.FragmentRepositoriesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BreakingNewsFragment : Fragment(R.layout.fragment_repositories), OnListItemClick {

    private val viewModel by viewModels<NewsViewModel>()

    private var _binding: FragmentRepositoriesBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentRepositoriesBinding.bind(view)

        val adapter = ItemsRecyclerViewAdapter(this)

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
            viewModel.getBreakingNews("us").collectLatest {
                adapter.submitData(it)
                adapter.notifyDataSetChanged()

            }
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
        setHasOptionsMenu(true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemFavorite(article: Article) {
        Log.e(TAG, "onItemFavorite $article")
        viewModel.addArticleToFavorite(article)
        Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show()
    }

    override fun onItemUnFavorite(article: Article) {
        Log.e(TAG, "onItemUnFavorite $article")

    }

    override fun onItemSelect(article: Article) {
        Log.e(TAG, "onItemSelect $article")
        val action = BreakingNewsFragmentDirections.actionBreakingNewsToDetailsFragment(article)
        findNavController().navigate(action)
    }

    companion object {
        const val TAG: String = "BreakingNewsFragment"
    }

}