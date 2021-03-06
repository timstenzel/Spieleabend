package de.stenzel.tim.spieleabend.presentation.news

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.NewsFragmentBinding
import de.stenzel.tim.spieleabend.helpers.Status
import de.stenzel.tim.spieleabend.helpers.isNetworkAvailable
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private var _binding: NewsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by activityViewModels()

    @Inject
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = NewsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        val manager : RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.newsRv.layoutManager = manager
        binding.newsRv.adapter = newsAdapter

        newsAdapter.onItemClick = { newsModel ->
            val action = NewsFragmentDirections.actionNewsFragmentToNewsDetailFragment(
                newsModel.img, newsModel.publisher, newsModel.title, newsModel.content, newsModel.publishDate!!
            )
            findNavController().navigate(action)
        }

        viewModel.filteredNews.observe(viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    hideProgressbar()
                    response.data?.let { newsList ->
                        newsAdapter.setData(newsList)
                    }
                }
                Status.ERROR -> {
                    hideProgressbar()
                    response.message?.let { message ->
                        showErrorView(message)
                    }
                }
                Status.LOADING -> {
                    showProgressbar()
                    hideErrorView()
                }
            }
        })

        binding.errorView.errorRetryBtn.setOnClickListener {
            startLoading()
        }
    }

    private fun startLoading() {
        showProgressbar()
        if (isNetworkAvailable(requireContext())) {
            viewModel.getAllNews()
        } else {
            hideProgressbar()
            showErrorView("No internet connection")
        }
    }

    private fun showProgressbar() {
        binding.progressbar.root.show()
    }

    private fun hideProgressbar() {
        binding.progressbar.root.hide()
    }

    private fun showErrorView(message: String) {
        binding.newsRv.visibility = View.GONE
        binding.errorView.root.visibility = View.VISIBLE
        binding.errorView.errorTextview.text = message
    }

    private fun hideErrorView() {
        binding.newsRv.visibility = View.VISIBLE
        binding.errorView.root.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu_news_filter, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.news_filter -> {
                findNavController().navigate(R.id.action_newsFragment_to_newsFilterFragment)
                true
            }
            else -> {
                Log.e("News Fragment", "error in options menu")
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}