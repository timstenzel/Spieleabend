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
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.AssistantFragmentBinding
import de.stenzel.tim.spieleabend.models.AssistantModel
import java.lang.IllegalArgumentException
import javax.inject.Inject

@AndroidEntryPoint
class AssistantFragment : Fragment() {

    private var _binding: AssistantFragmentBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var assistantAdapter: AssistantAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = AssistantFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfItems = setupList()

        val manager : RecyclerView.LayoutManager = GridLayoutManager(context, 2)
        binding.assistantRv.layoutManager = manager
        binding.assistantRv.adapter = assistantAdapter

        assistantAdapter.setData(listOfItems)

        assistantAdapter.onItemClick = { assistantModel ->

            val items = resources.getStringArray(R.array.assistant_items)

            when(assistantModel.title) {
                items[0] -> { //dominion deck generator
                    //TODO navigate to generator
                }
                items[1] -> { // who begins
                    //TODO navigate to assistant sub items
                }
                items[2] -> { // game catalogue
                    //TODO navigate to game catalogue
                }
                else -> {
                    Log.e("AssistantFgmt", "Title does not match")
                }
            }

        }
    }

    private fun setupList(): List<AssistantModel> {
        val images = resources.obtainTypedArray(R.array.assistant_images)
        val items = resources.getStringArray(R.array.assistant_items)

        val listOfAssistantItems = arrayListOf<AssistantModel>()

        if (images.length() != items.size) {
            throw IllegalArgumentException("No equal amount of images and items")
        } else {
            for (item in items.withIndex()) {
                val drawable = images.getDrawable(item.index)
                listOfAssistantItems.add(AssistantModel(drawable!!, items[item.index]))
            }
        }
        images.recycle()
        return listOfAssistantItems
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}