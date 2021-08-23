package de.stenzel.tim.spieleabend.presentation.assistant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.databinding.AssistantFragmentBinding
import javax.inject.Inject

@AndroidEntryPoint
class AssistantFragment : Fragment() {

    private var _binding: AssistantFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AssistantViewModel by viewModels()

    @Inject
    lateinit var assistantAdapter: AssistantAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = AssistantFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager : RecyclerView.LayoutManager = GridLayoutManager(context, 2)
        binding.assistantRv.layoutManager = manager
        binding.assistantRv.adapter = assistantAdapter

        assistantAdapter.onItemClick = { assistantModel ->
            when(assistantModel.title) {
                "Dominion Deck Generator" -> {
                    //TODO navigate to generator
                }
                "Wer beginnt?" -> {
                    //TODO navigate to assistant sub items
                }
                else -> {
                    Log.e("AssistantFgmt", "Title does not match")
                }
            }

        }

        viewModel.items.observe(viewLifecycleOwner, Observer { items ->
            assistantAdapter.setData(items)
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}