package de.stenzel.tim.spieleabend.presentation.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.NewsDetailFragmentBinding
import de.stenzel.tim.spieleabend.glide.GlideApp
import de.stenzel.tim.spieleabend.helpers.formatTimstampToDateString

@AndroidEntryPoint
class NewsDetailFragment : Fragment() {

    private var _binding : NewsDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val args: NewsDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = NewsDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.img.isEmpty()) {
            GlideApp.with(requireContext()).load(R.drawable.news_default).into(binding.newsDetailIv)
        } else {
            val ref = FirebaseStorage.getInstance().getReferenceFromUrl(args.img)
            GlideApp.with(requireContext()).load(ref).error(R.drawable.error_default).into(binding.newsDetailIv)
        }
        binding.newsDetailPublisherTv.text = args.publisher
        binding.newsDetailDateTv.text = formatTimstampToDateString(args.publishDate)
        binding.newsDetailTitleTv.text = args.title
        binding.newsDetailContentTv.text = args.content
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}