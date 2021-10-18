package de.stenzel.tim.spieleabend.presentation.events

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
import de.stenzel.tim.spieleabend.databinding.EventFragmentBinding
import de.stenzel.tim.spieleabend.helpers.Resource
import de.stenzel.tim.spieleabend.helpers.Status
import de.stenzel.tim.spieleabend.helpers.isNetworkAvailable
import javax.inject.Inject

@AndroidEntryPoint
class EventFragment : Fragment() {

    private var _binding: EventFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventViewModel by viewModels()

    @Inject
    lateinit var eventsAdapter : EventAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = EventFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager : RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.eventsRv.hasFixedSize()
        binding.eventsRv.layoutManager = manager
        binding.eventsRv.adapter = eventsAdapter

        binding.eventsRv.addItemDecoration(StickyHeaderItemDecoration(binding.eventsRv, eventsAdapter))

        eventsAdapter.onEventItemClick = { eventModel ->
            val action = EventFragmentDirections.actionEventFragmentToEventDetailFragment(
                eventModel.img, eventModel.title, eventModel.description, eventModel.startDate,
                eventModel.endDate, eventModel.publisher, eventModel.location, "133 km")
            findNavController().navigate(action)
        }

        viewModel.events.observe(viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    hideProgressbar()
                    response.data?.let { eventList ->
                        eventsAdapter.setData(eventList)
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

        startLoading()
    }

    private fun startLoading() {
        showProgressbar()
        if (isNetworkAvailable(requireContext())) {
            viewModel.getAllEvents()
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
        binding.eventsRv.visibility = View.GONE
        binding.errorView.root.visibility = View.VISIBLE
        binding.errorView.errorTextview.text = message
    }

    private fun hideErrorView() {
        binding.eventsRv.visibility = View.VISIBLE
        binding.errorView.root.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}