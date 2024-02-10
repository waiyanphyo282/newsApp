package com.waiyanphyo.betternews.ui.newsList

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.waiyanphyo.betternews.R
import com.waiyanphyo.betternews.databinding.FragmentNewsListBinding
import com.waiyanphyo.betternews.ui.adapters.ArticleAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsListFragment : Fragment(R.layout.fragment_news_list) {

    private lateinit var binding: FragmentNewsListBinding
    private val viewModel by viewModels<NewsListViewModel>()
    private val articleAdapter by lazy { ArticleAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentNewsListBinding.bind(view)

        binding.rvNews.adapter = articleAdapter

        articleAdapter.setOnItemClickListener {article ->
            article.id?.let {
                findNavController().navigate(NewsListFragmentDirections.actionNewsListFragmentToNewsDetailFragment(it))
            }
        }

        binding.apply {

            searchView.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    btnSearch.callOnClick()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    btnSearch.isEnabled = !newText.isNullOrEmpty()
                    return true
                }

            })

            btnSearch.setOnClickListener {
                viewModel.changeSearchQuery(searchView.query.toString())
                articleAdapter.refresh()
            }

            swipeRefreshLayout.setOnRefreshListener {
                articleAdapter.refresh()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.articlesFlow.collect {
                        binding.swipeRefreshLayout.isRefreshing = false
                        articleAdapter.submitData(it)
                    }
                }
                launch {
                    articleAdapter.loadStateFlow.collect {loadState ->
                        val isListEmpty = loadState.refresh is LoadState.NotLoading && articleAdapter.itemCount == 0
                        // show empty list
                        binding.tvEmpty.isVisible = isListEmpty
                        // Only show the list if refresh succeeds, either from the the local db or the remote.
                        binding.rvNews.isVisible =  loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                        // Show loading spinner during initial load or refresh.
                        binding.progressCircular.isVisible = loadState.mediator?.refresh is LoadState.Loading
                    }
                }
            }
        }
    }


}