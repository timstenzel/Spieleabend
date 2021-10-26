package de.stenzel.tim.spieleabend.presentation.assistant

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.DominionDeckFragmentBinding

@AndroidEntryPoint
class DominionDeckFragment : Fragment() {

    private var _binding: DominionDeckFragmentBinding? = null
    private val binding get() = _binding!!

    private val args: DominionDeckFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DominionDeckFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val cards = args.generatedDeck.cards
        val event = args.generatedDeck.event
        val monument = args.generatedDeck.monument

        binding.dominionDeckCard1.text = cards[0].title
        binding.dominionDeckCard2.text = cards[1].title
        binding.dominionDeckCard3.text = cards[2].title
        binding.dominionDeckCard4.text = cards[3].title
        binding.dominionDeckCard5.text = cards[4].title
        binding.dominionDeckCard6.text = cards[5].title
        binding.dominionDeckCard7.text = cards[6].title
        binding.dominionDeckCard8.text = cards[7].title
        binding.dominionDeckCard9.text = cards[8].title
        binding.dominionDeckCard10.text = cards[9].title
        binding.dominionDeckEvent.text = event.title
        binding.dominionDeckMonument.text = monument.title



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}