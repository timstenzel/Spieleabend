package de.stenzel.tim.spieleabend.presentation.assistant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.databinding.CatalogueFragmentBinding
import de.stenzel.tim.spieleabend.helpers.isNetworkAvailable
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CatalogueFragment : Fragment() {

    private var _binding: CatalogueFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CatalogueViewModel by viewModels()

    @Inject
    lateinit var catalogueAdapter: CatalogueAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = CatalogueFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val manager : RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.catalogueRv.hasFixedSize()
        binding.catalogueRv.layoutManager = manager

        val loaderStateAdapter = LoaderStateAdapter { catalogueAdapter.retry()}
        binding.catalogueRv.adapter = catalogueAdapter.withLoadStateFooter(loaderStateAdapter)

        catalogueAdapter.onItemClick = { game ->
            //disable click on item while loading
            if (!binding.progressbar.root.isVisible){
                val action = CatalogueFragmentDirections.actionCatalogueFragmentToCatalogueDetailFragment(game.id)
                findNavController().navigate(action)
            }
        }

        //add a listener for load state to display a progressbar
        catalogueAdapter.addLoadStateListener { loadState ->
            Log.d("CFrag", "loading: ${loadState.refresh}")

            if (loadState.refresh is LoadState.Loading) {
                binding.progressbar.root.show()
            } else if (loadState.refresh is LoadState.NotLoading) {
                binding.progressbar.root.hide()
            }
        }

        binding.errorView.errorRetryBtn.setOnClickListener {
            fetchBoardgames()
        }

        fetchBoardgames()
    }

    // FIXME: 12.10.21 avoid reload of paging data after return from detail fragment
    // FIXME: 28.10.21 better than before, but still loads current page again
    private fun fetchBoardgames() {
        if (viewModel.paginatedLiveData == null) {
            showProgressbar()
            if (isNetworkAvailable(requireContext())) {
                lifecycleScope.launch {
                    viewModel.fetchBoardgames().observe(viewLifecycleOwner, Observer {
                        lifecycleScope.launch {
                            catalogueAdapter.submitData(it)
                        }
                    })
                }
            } else {
                hideProgressbar()
                showErrorView("No internet connection")
            }
        }
    }

    private fun showProgressbar() {
        binding.progressbar.root.show()
    }

    private fun hideProgressbar() {
        binding.progressbar.root.hide()
    }

    private fun showErrorView(message: String) {
        binding.catalogueRv.visibility = View.GONE
        binding.errorView.root.visibility = View.VISIBLE
        binding.errorView.errorTextview.text = message
    }

    private fun hideErrorView() {
        binding.catalogueRv.visibility = View.VISIBLE
        binding.errorView.root.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}