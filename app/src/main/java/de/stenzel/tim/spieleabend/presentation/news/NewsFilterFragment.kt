package de.stenzel.tim.spieleabend.presentation.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.NewsFilterFragmentBinding
import de.stenzel.tim.spieleabend.models.Filter

@AndroidEntryPoint
class NewsFilterFragment : Fragment() {

    private var _binding: NewsFilterFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by activityViewModels()

    private val publishers = mutableListOf<String>()
    private val topics = mutableListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = NewsFilterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //sort by
        val sort = resources.getStringArray(R.array.sort_by)
        //publishers
        publishers.clear()
        val data = viewModel.news.value?.data
        publishers.add(getString(R.string.filter_show_all))
        for (item in data!!) {
            if (!publishers.contains(item.publisher)) {
                publishers.add(item.publisher!!)
            }
        }
        //topics
        topics.clear()
        topics.add(getString(R.string.filter_show_all))
        topics.addAll(resources.getStringArray(R.array.news_topics))

        //set default
        val sortSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sort)
        val filterPublisherAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, publishers)
        val filterTopicAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, topics)

        binding.newsFilterSortBySpinner.adapter = sortSpinnerAdapter
        binding.newsFilterPublisherSpinner.adapter = filterPublisherAdapter
        binding.newsFilterTopicSpinner.adapter = filterTopicAdapter

        viewModel.filters.observe(viewLifecycleOwner, { map ->

            if (map.isNullOrEmpty()) {
                binding.newsFilterSortBySpinner.setSelection(0)
                binding.newsFilterPublisherSpinner.setSelection(0)
                binding.newsFilterTopicSpinner.setSelection(0)

            } else {
                //update sort by spinner
                when {
                    map.containsKey(Filter.SORT_BY_DATE_DESC) -> {
                        binding.newsFilterSortBySpinner.setSelection(0)
                    }
                    map.containsKey(Filter.SORT_BY_DATE_ASC) -> {
                        binding.newsFilterSortBySpinner.setSelection(1)
                    }
                    map.containsKey(Filter.SORT_BY_A_TO_Z) -> {
                        binding.newsFilterSortBySpinner.setSelection(2)
                    }
                    map.containsKey(Filter.SORT_BY_Z_TO_A) -> {
                        binding.newsFilterSortBySpinner.setSelection(3)
                    }
                }
                //update filter publisher spinner
                when {
                    map.containsKey(Filter.FILTER_PUBLISHER) -> {
                        val index = publishers.indexOf(map[Filter.FILTER_PUBLISHER])
                        binding.newsFilterPublisherSpinner.setSelection(index)
                    }
                }
                //update filter topic spinner
                when {
                    map.containsKey(Filter.FILTER_TOPIC) -> {
                        val index = topics.indexOf(map[Filter.FILTER_TOPIC])
                        binding.newsFilterTopicSpinner.setSelection(index)
                    }
                }
            }
        })

        binding.newsFilterApply.setOnClickListener {
            viewModel.removeFilter()
            //sort by
            val sortByArr = resources.getStringArray(R.array.sort_by)
            val sortPos = binding.newsFilterSortBySpinner.selectedItemPosition
            val sortBy = sortByArr[sortPos]
            val sortPair = Pair(sortPos, sortBy)

            //filter publisher
            val filterPublisherPair = when (val filterPos = binding.newsFilterPublisherSpinner.selectedItemPosition) {
                0 -> {
                    Pair(Filter.FILTER_PUBLISHER, Filter.FILTER_PUBLISHER_ALL)
                }
                else -> {
                    Pair(Filter.FILTER_PUBLISHER, publishers[filterPos])
                }
            }

            //filter topic
            val filterTopicPair = when (val filterPos = binding.newsFilterTopicSpinner.selectedItemPosition) {
                0 -> {
                    Pair(Filter.FILTER_TOPIC, Filter.FILTER_TOPIC_ALL)
                }
                else -> {
                    Pair(Filter.FILTER_TOPIC, topics[filterPos])
                }
            }

            val filters = mutableMapOf<Int, String>()
            filters[sortPair.first] = sortPair.second
            filters[filterPublisherPair.first] = filterPublisherPair.second
            filters[filterTopicPair.first] = filterTopicPair.second

            viewModel.addFilters(filters)
            findNavController().popBackStack(R.id.newsFragment, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}