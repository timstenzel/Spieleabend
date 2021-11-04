package de.stenzel.tim.spieleabend.presentation.assistant

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ApplicationContext
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.CatalogueItemBinding
import de.stenzel.tim.spieleabend.glide.GlideApp
import de.stenzel.tim.spieleabend.models.remote.Game
import javax.inject.Inject

class CatalogueAdapter @Inject constructor(@ApplicationContext private val context: Context) : PagingDataAdapter<Game, CatalogueAdapter.CatalogueItemViewHolder>(DiffUtilCallback) {

    var onItemClick: ((Game) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogueItemViewHolder {
        val binding = CatalogueItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogueItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatalogueItemViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class CatalogueItemViewHolder(private val binding: CatalogueItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(absoluteAdapterPosition)!!)
            }
        }

        fun bind(model : Game) {
            if (model.thumb_url.isEmpty()) {
                GlideApp.with(itemView).load(R.drawable.news_default).into(binding.catalogueItemImage)
            } else {
                GlideApp.with(itemView).load(model.thumb_url).error(R.drawable.error_default).into(binding.catalogueItemImage)
            }
            binding.catalogueItemTitle.text = model.name

            val players = if (model.min_players == model.max_players) {
                context.getString(R.string.catalogue_player, model.max_players)
            } else {
                context.getString(R.string.catalogue_players, model.min_players, model.max_players)
            }
            binding.catalogueItemPlayers.text = players
            binding.catalogueItemTime.text = context.getString(R.string.catalogue_time, model.min_playtime, model.max_playtime)
        }

    }

    object DiffUtilCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(
            oldItem: Game,
            newItem: Game
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Game,
            newItem: Game
        ): Boolean {
            return oldItem == newItem
        }


    }
}