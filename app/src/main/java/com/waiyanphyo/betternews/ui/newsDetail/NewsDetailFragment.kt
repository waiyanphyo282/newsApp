package com.waiyanphyo.betternews.ui.newsDetail

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.waiyanphyo.betternews.R
import com.waiyanphyo.betternews.databinding.FragmentNewsDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {

    private val viewModel by viewModels<NewsDetailViewModel>()
    private lateinit var binding: FragmentNewsDetailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsDetailBinding.bind(view)

        viewModel.uiState.observe(viewLifecycleOwner) {uiState ->
            when (uiState) {
                NewsDetailUiState.Loading -> {
                    binding.progressCircular.show()
                }
                is NewsDetailUiState.Error ->  {
                    binding.progressCircular.hide()
                }
                is NewsDetailUiState.Success -> {
                    binding.apply {
                        progressCircular.hide()
                        tvTitle.text = uiState.article.title
                        ivArticle.load(uiState.article.urlToImage)
                        tvContent.text = uiState.article.content
                        tvViewContent.setOnClickListener {
                            val intent = CustomTabsIntent.Builder()
                                .build()
                            intent.launchUrl(requireContext(), Uri.parse(uiState.article.url))
                        }
                    }
                    Timber.d("Article ${uiState.article}")
                }
            }
        }
    }

}