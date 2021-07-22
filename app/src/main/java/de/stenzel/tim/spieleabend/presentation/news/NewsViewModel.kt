package de.stenzel.tim.spieleabend.presentation.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import de.stenzel.tim.spieleabend.helpers.Constants
import de.stenzel.tim.spieleabend.models.NewsModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject
constructor() : ViewModel() {

    private val TAG = "NewsViewModel"

    //@Inject
    //lateinit var db : DatabaseReference
    private val db = FirebaseDatabase.getInstance(Constants.URL_FIRE_DB).getReference("news_list")

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

                _news.postValue(list)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "retrieval from db failed")
            }
        }

        db.addValueEventListener(listener)

    /*
        viewModelScope.launch {

            repository.getAllNews().let { response: Response<NewsList> ->
                if (response.isSuccessful) {
                    _news.postValue(response.body())
                } else {
                    Log.d(TAG, "getAllNews error: ${response.code()}")
                }
            }
        }

         */
    }


}