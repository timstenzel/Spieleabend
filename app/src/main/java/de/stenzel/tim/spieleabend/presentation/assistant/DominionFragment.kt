package de.stenzel.tim.spieleabend.presentation.assistant

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.DominionFragmentBinding
import de.stenzel.tim.spieleabend.glide.GlideApp
import de.stenzel.tim.spieleabend.helpers.Status
import de.stenzel.tim.spieleabend.helpers.isNetworkAvailable
import javax.inject.Inject

@AndroidEntryPoint
class DominionFragment : Fragment() {

    private var _binding: DominionFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DominionViewModel by viewModels()

    @Inject
    lateinit var dominionAdapter: DominionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DominionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val manager : RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.dominionExpandableRv.hasFixedSize()
        binding.dominionExpandableRv.layoutManager = manager
        binding.dominionExpandableRv.adapter = dominionAdapter
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.dominionExpandableRv.addItemDecoration(decoration)

        viewModel.dominionAll.observe(viewLifecycleOwner, Observer { event ->
            val resource = event.getContentIfNotHandled()
            resource?.let { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        hideProgressbar()
                        response.data?.let { dominionModel ->
                            viewModel.prepareDataForExpandableAdapter(dominionModel)
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
            }
        })

        viewModel.preparedDominionExpansionData.observe(viewLifecycleOwner, Observer { data ->
            dominionAdapter.setData(data)
        })

        viewModel.generatedDeck.observe(viewLifecycleOwner, Observer { event ->
            val resource = event.getContentIfNotHandled()
            resource?.let { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        hideProgressbar()
                        response.data?.let { deck ->
                            //pass deck to next fragment
                            val action = DominionFragmentDirections.actionDominionFragmentToDominionDeckFragment(deck)
                            findNavController().navigate(action)
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
            }
        })

        binding.dominionConfirmBtn.setOnClickListener {
            viewModel.generateDeck(dominionAdapter.getData())
        }

        binding.errorView.errorRetryBtn.setOnClickListener {
            startLoading()
        }

        startLoading()
    }

    private fun startLoading() {
        showProgressbar()
        if (isNetworkAvailable(requireContext())) {
            viewModel.loadData()
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
        binding.dominionExpandableRv.visibility = View.GONE
        binding.dominionConfirmBtn.visibility = View.GONE
        binding.errorView.root.visibility = View.VISIBLE
        binding.errorView.errorTextview.text = message
    }

    private fun hideErrorView() {
        binding.dominionExpandableRv.visibility = View.VISIBLE
        binding.dominionConfirmBtn.visibility = View.VISIBLE
        binding.errorView.root.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}