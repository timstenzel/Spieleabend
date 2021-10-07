package de.stenzel.tim.spieleabend.presentation.assistant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import de.stenzel.tim.spieleabend.databinding.CatalogueBoardgameLoaderBinding

class LoaderStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoaderStateAdapter.LoaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
        val binding = CatalogueBoardgameLoaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoaderViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoaderViewHolder(private val binding: CatalogueBoardgameLoaderBinding, retry: () -> Unit) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener {
                retry()
            }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Loading) {
                binding.catalogueLoaderMl.transitionToEnd()
            } else {
                binding.catalogueLoaderMl.transitionToStart()
            }
        }

    }

}