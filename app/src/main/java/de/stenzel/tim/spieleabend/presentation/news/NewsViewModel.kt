package de.stenzel.tim.spieleabend.presentation.news

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import de.stenzel.tim.spieleabend.helpers.Constants
import de.stenzel.tim.spieleabend.models.NewsModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject
constructor(
    private val db: FirebaseDatabase
) : ViewModel() {

    private val TAG = "NewsViewModel"

    private val _news = MutableLiveData<ArrayList<NewsModel>>()
    val news : LiveData<ArrayList<NewsModel>>
        get() = _news

    init {
        getAllNews()
    }


    private fun getAllNews() {


        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<NewsModel>()
                for (sn : DataSnapshot in snapshot.children) {
                    val news = sn.getValue(NewsModel::class.java)
                    list.add(news!!)
                }
                //filter list by date desc (oldest at bottom)
                list.sortByDescending { it.publishDate }
                _news.postValue(list)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "retrieval from db failed")
            }
        }

        db.getReference("news_list").addValueEventListener(listener)
    }


}