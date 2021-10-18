package de.stenzel.tim.spieleabend.presentation.news

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import de.stenzel.tim.spieleabend.helpers.Constants
import de.stenzel.tim.spieleabend.helpers.Resource
import de.stenzel.tim.spieleabend.helpers.isNetworkAvailable
import de.stenzel.tim.spieleabend.models.NewsModel
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

    fun getAllNews() {
        try {
            _news.postValue(Resource.loading(null))

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _news.postValue(handleNewsResponse(snapshot))
                }

                override fun onCancelled(error: DatabaseError) {
                    _news.postValue(Resource.error(error.message, null))
                }
            }
            db.getReference("news_list").addValueEventListener(listener)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _news.postValue(Resource.error("Network failure", null))
                }
                else -> {
                    _news.postValue(Resource.error("Conversion error", null))
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
        //filter list by date desc (oldest at bottom)
        list.sortByDescending { it.publishDate }

        return Resource.success(list)
    }
}