package de.stenzel.tim.spieleabend.presentation.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.NewsItemBinding
import de.stenzel.tim.spieleabend.models.NewsModel

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsItemViewHolder>() {

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
        fun bind(model: NewsModel) {
            Glide.with(itemView).load(R.drawable.news_default).into(binding.newsItemIv)
            binding.newsItemPublisherTv.text = model.publisher
            binding.newsItemDateTv.text = model.publishedAt
            binding.newsItemTitleTv.text = model.title
        }
    }
}