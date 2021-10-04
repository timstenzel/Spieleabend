package de.stenzel.tim.spieleabend.presentation.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.databinding.NewsFragmentBinding
import de.stenzel.tim.spieleabend.models.NewsModel
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private var _binding: NewsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels()

    @Inject
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = NewsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager : RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.newsRv.layoutManager = manager
        binding.newsRv.adapter = newsAdapter

        newsAdapter.onItemClick = { newsModel ->
            val action = NewsFragmentDirections.actionNewsFragmentToNewsDetailFragment(newsModel.img, newsModel.publisher, newsModel.title, newsModel.content, newsModel.publishDate)
            findNavController().navigate(action)
        }

        viewModel.news.observe(viewLifecycleOwner, Observer { newsList ->
            newsAdapter.setData(newsList)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { loading ->
            when(loading) {
                true -> binding.progressbar.root.show()
                false -> binding.progressbar.root.hide()
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}