package de.stenzel.tim.spieleabend.presentation.assistant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import de.stenzel.tim.spieleabend.databinding.CatalogueItemBinding
import de.stenzel.tim.spieleabend.models.BoardgameWrapper2

class CatalogueAdapter : PagingDataAdapter<BoardgameWrapper2.Game, CatalogueAdapter.CatalogueItemViewHolder>(DiffUtilCallback) {

    var onItemClick: ((BoardgameWrapper2.Game) -> Unit)? = null

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

        fun bind(model : BoardgameWrapper2.Game, position: Int) {
            binding.catalogueGameName.text = "$position: ${model.name}"
        }

    }

    object DiffUtilCallback : DiffUtil.ItemCallback<BoardgameWrapper2.Game>() {
        override fun areItemsTheSame(
            oldItem: BoardgameWrapper2.Game,
            newItem: BoardgameWrapper2.Game
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: BoardgameWrapper2.Game,
            newItem: BoardgameWrapper2.Game
        ): Boolean {
            return oldItem == newItem
        }


    }
}