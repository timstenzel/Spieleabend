package de.stenzel.tim.spieleabend.presentation.assistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.CatalogueDetailFragmentBinding

@AndroidEntryPoint
class CatalogueDetailFragment : Fragment() {

    private var _binding: CatalogueDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val args: CatalogueDetailFragmentArgs by navArgs()

    private val viewModel: CatalogueDetailViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = CatalogueDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.game.observe(viewLifecycleOwner, Observer { wrapper ->
            //Glide.with(requireContext()).load(game.images.large).into(binding.catalogueDetailImage)
            Glide.with(requireContext()).load(R.drawable.error_default).into(binding.catalogueDetailImage)
            binding.catalogueDetailTitle.text = wrapper.games.first().name
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { loading ->
            when (loading) {
                true -> {binding.progressbar.root.show()}
                false -> {binding.progressbar.root.hide()}
            }
        })

        if (args.id.isEmpty()) {
            // TODO: 07.10.21 show error view
        } else {
            //start load of game details
            viewModel.loadGameDetails(args.id)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}