package de.stenzel.tim.spieleabend.presentation.events

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.EventDetailFragmentBinding
import de.stenzel.tim.spieleabend.databinding.NewsDetailFragmentBinding
import de.stenzel.tim.spieleabend.glide.GlideApp
import de.stenzel.tim.spieleabend.helpers.formatTimstampToDateString
import de.stenzel.tim.spieleabend.helpers.timestampToLocalDateTime
import de.stenzel.tim.spieleabend.presentation.news.NewsDetailFragmentArgs
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class EventDetailFragment : Fragment() {

    private var _binding : EventDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val args: EventDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = EventDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val event = args.eventModel

        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        if (event.img.isNullOrEmpty()) {
            GlideApp.with(requireContext()).load(R.drawable.event_default).into(binding.eventDetailImage)
        } else {
            val ref = FirebaseStorage.getInstance().getReferenceFromUrl(event.img)
            GlideApp.with(requireContext()).load(ref).error(R.drawable.error_default).into(binding.eventDetailImage)
        }
        binding.eventDetailPublisher.text = getString(R.string.event_publisher, event.publisher)
        binding.eventDetailTitle.text = event.title

        if (event.startDate != null && event.endDate != null) {
            val startDate = timestampToLocalDateTime(event.startDate)
            val endDate = timestampToLocalDateTime(event.endDate)
            binding.eventDetailDate.text = startDate.format(dateFormatter)
            binding.eventDetailTime.text = getString(R.string.event_time, startDate.format(timeFormatter), endDate.format(timeFormatter))
        }

        binding.eventDetailLocation.text = event.location
        binding.eventDetailDistance.text = event.distance
        binding.eventDetailContent.text = event.description
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}