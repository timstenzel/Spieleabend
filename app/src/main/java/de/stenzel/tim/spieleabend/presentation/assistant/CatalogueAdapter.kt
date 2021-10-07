package de.stenzel.tim.spieleabend.presentation.assistant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import de.stenzel.tim.spieleabend.databinding.CatalogueItemBinding
import de.stenzel.tim.spieleabend.models.BoardgameWrapper

class CatalogueAdapter : PagingDataAdapter<BoardgameWrapper.Game, CatalogueAdapter.CatalogueItemViewHolder>(DiffUtilCallback) {

    var onItemClick: ((BoardgameWrapper.Game) -> Unit)? = null

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

        fun bind(model : BoardgameWrapper.Game) {
            binding.catalogueGameName.text = model.name
        }

    }

    object DiffUtilCallback : DiffUtil.ItemCallback<BoardgameWrapper.Game>() {
        override fun areItemsTheSame(
            oldItem: BoardgameWrapper.Game,
            newItem: BoardgameWrapper.Game
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: BoardgameWrapper.Game,
            newItem: BoardgameWrapper.Game
        ): Boolean {
            return oldItem == newItem
        }


    }
}