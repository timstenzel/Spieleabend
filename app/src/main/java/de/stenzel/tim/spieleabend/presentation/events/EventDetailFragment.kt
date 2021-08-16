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

        val startDate = timestampToLocalDateTime(args.startDate)
        val endDate = timestampToLocalDateTime(args.endDate)
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        if (args.img.isEmpty()) {
            GlideApp.with(requireContext()).load(R.drawable.event_default).into(binding.eventDetailImage)
        } else {
            val ref = FirebaseStorage.getInstance().getReferenceFromUrl(args.img)
            GlideApp.with(requireContext()).load(ref).error(R.drawable.error_default).into(binding.eventDetailImage)
        }
        binding.eventDetailPublisher.text = "${args.publisher} präsentiert"
        binding.eventDetailTitle.text = args.title
        binding.eventDetailDate.text = startDate.format(dateFormatter)
        binding.eventDetailTime.text = "${startDate.format(timeFormatter)} - ${endDate.format(timeFormatter)}"
        binding.eventDetailLocation.text = args.location
        binding.eventDetailDistance.text = args.distance
        binding.eventDetailContent.text = args.description
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}