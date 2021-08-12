package de.stenzel.tim.spieleabend.presentation.events

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.EventFragmentBinding
import de.stenzel.tim.spieleabend.models.EventHeader
import de.stenzel.tim.spieleabend.models.EventModel
import de.stenzel.tim.spieleabend.presentation.news.NewsFragmentDirections
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
            Toast.makeText(context, "Event clicked: ${eventModel.date}", Toast.LENGTH_SHORT).show()
        }

        viewModel.events.observe(viewLifecycleOwner, Observer { eventList ->
            eventsAdapter.setData(eventList)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}