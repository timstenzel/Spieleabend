package de.stenzel.tim.spieleabend.presentation.assistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import de.stenzel.tim.spieleabend.databinding.FirstPlayerFragmentBinding

class FirstPlayerFragment : Fragment() {

    private var _binding: FirstPlayerFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FirstPlayerViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FirstPlayerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}