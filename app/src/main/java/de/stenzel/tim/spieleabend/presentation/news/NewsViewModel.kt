package de.stenzel.tim.spieleabend.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import de.stenzel.tim.spieleabend.helpers.Resource
import de.stenzel.tim.spieleabend.models.local.Filter
import de.stenzel.tim.spieleabend.models.remote.NewsModel
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val db: FirebaseDatabase
) : ViewModel() {

    private val TAG = "NewsViewModel"

    private val _news = MutableLiveData<Resource<List<NewsModel>>>()
    val news : LiveData<Resource<List<NewsModel>>>
        get() = _news

    private val _filteredNews = MutableLiveData<Resource<List<NewsModel>>>()
    val filteredNews : LiveData<Resource<List<NewsModel>>>
        get() = _filteredNews


    private val _filters = MutableLiveData<Map<Int, String>>()
    val filters : LiveData<Map<Int, String>>
        get() = _filters

    init {
        getAllNews()
    }

    fun getAllNews() {
        try {
            _filteredNews.postValue(Resource.loading(null))

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val news = handleNewsResponse(snapshot)
                    _news.postValue(news)
                    _filteredNews.postValue(news)
                }

                override fun onCancelled(error: DatabaseError) {
                    _filteredNews.postValue(Resource.error(error.message, null))
                }
            }
            db.getReference("news_list").addValueEventListener(listener)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _filteredNews.postValue(Resource.error("Network failure", null))
                }
                else -> {
                    _filteredNews.postValue(Resource.error("Conversion error", null))
                }
            }
        }
    }

    private fun handleNewsResponse(snapshot: DataSnapshot) : Resource<List<NewsModel>> {

        val list = arrayListOf<NewsModel>()
        for (sn : DataSnapshot in snapshot.children) {
            val news = sn.getValue(NewsModel::class.java)
            list.add(news!!)
        }
        return Resource.success(list.sortedByDescending { it.publishDate })
    }

    private fun applyFilters(filters: Map<Int, String>) : Resource<List<NewsModel>> {
        //filter list
        val allNews = news.value?.data
        if (allNews != null) {
            if (filters.isNullOrEmpty()) {
                //no filters set -> show default (news by all publishers and oldest at bottom)
                return Resource.success(allNews.sortedByDescending { it.publishDate })
            } else {
                //some filters set
                var filteredList = when {
                    filters.containsKey(Filter.SORT_BY_A_TO_Z) -> {
                        allNews.sortedBy { it.title }
                    }
                    filters.containsKey(Filter.SORT_BY_Z_TO_A) -> {
                        allNews.sortedByDescending { it.title }
                    }
                    filters.containsKey(Filter.SORT_BY_DATE_ASC) -> {
                        allNews.sortedBy { it.publishDate }
                    }
                    filters.containsKey(Filter.SORT_BY_DATE_DESC) -> {
                        allNews.sortedByDescending { it.publishDate }
                    }
                    else -> {
                        allNews
                    }
                }
                if (filters.containsKey(Filter.FILTER_PUBLISHER)) {
                    filteredList = when (filters[Filter.FILTER_PUBLISHER]) {
                        Filter.FILTER_PUBLISHER_ALL -> {
                            filteredList
                        }
                        else -> {
                            filteredList.filter { it.publisher == filters[Filter.FILTER_PUBLISHER] }
                        }
                    }
                }
                if (filters.containsKey(Filter.FILTER_TOPIC)) {
                    filteredList = when (filters[Filter.FILTER_TOPIC]) {
                        Filter.FILTER_TOPIC_ALL -> {
                            filteredList
                        }
                        else -> {
                            filteredList.filter { it.topic == filters[Filter.FILTER_TOPIC] }
                        }
                    }
                }

                //check list size for resource return
                return if (filteredList.isEmpty()) {
                    Resource.error("Keine Neuigkeiten übrig, bitte Filter anpassen", null)
                } else {
                    Resource.success(filteredList)
                }
            }
        } else {
            return Resource.error("news list was null", null)
        }
    }

    fun addFilters(filter: Map<Int, String>) {
        val filteredList = applyFilters(filter)
        _filteredNews.postValue(filteredList)
        _filters.postValue(filter)
    }

    fun removeFilter() {
        _filters.postValue(mapOf())
    }
}