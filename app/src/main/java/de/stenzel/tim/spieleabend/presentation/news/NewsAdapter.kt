package de.stenzel.tim.spieleabend.presentation.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.NewsItemBinding
import de.stenzel.tim.spieleabend.glide.GlideApp
import de.stenzel.tim.spieleabend.helpers.formatTimstampToDateString
import de.stenzel.tim.spieleabend.models.remote.NewsModel
import javax.inject.Inject

class NewsAdapter @Inject constructor() : RecyclerView.Adapter<NewsAdapter.NewsItemViewHolder>() {

    var onItemClick: ((NewsModel) -> Unit)? = null
    private val data = ArrayList<NewsModel>()

    fun setData(list: List<NewsModel>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class NewsItemViewHolder(private val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private val storage = FirebaseStorage.getInstance()

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(data[absoluteAdapterPosition])
            }
        }

        fun bind(model: NewsModel) {
            if (model.img.isNullOrEmpty()) {
                GlideApp.with(itemView).load(R.drawable.news_default).into(binding.newsItemIv)
            } else {
                val ref = storage.getReferenceFromUrl(model.img)
                GlideApp.with(itemView).load(ref).error(R.drawable.error_default).into(binding.newsItemIv)
            }
            binding.newsItemPublisherTv.text = model.publisher
            binding.newsItemDateTv.text = formatTimstampToDateString(model.publishDate?.let { model.publishDate } ?: -1L)
            binding.newsItemTitleTv.text = model.title
        }
    }
}