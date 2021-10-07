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

        fetchBoardgames()
    }

    private fun fetchBoardgames() {

        viewModel.fetchBoardgames().observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                catalogueAdapter.submitData(it)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}