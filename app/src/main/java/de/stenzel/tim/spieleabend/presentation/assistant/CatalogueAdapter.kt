package de.stenzel.tim.spieleabend.presentation.assistant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import de.stenzel.tim.spieleabend.databinding.CatalogueItemBinding
import de.stenzel.tim.spieleabend.models.Game

class CatalogueAdapter : PagingDataAdapter<Game, CatalogueAdapter.CatalogueItemViewHolder>(DiffUtilCallback) {

    var onItemClick: ((Game) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogueItemViewHolder {
        val binding = CatalogueItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogueItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatalogueItemViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, position) }
    }

    inner class CatalogueItemViewHolder(private val binding: CatalogueItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(absoluteAdapterPosition)!!)
            }
        }

        fun bind(model : Game, position: Int) {
            binding.catalogueGameName.text = "$position: ${model.name}"
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