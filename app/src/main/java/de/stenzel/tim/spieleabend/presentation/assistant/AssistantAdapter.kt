package de.stenzel.tim.spieleabend.presentation.assistant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.stenzel.tim.spieleabend.databinding.AssistantItemBinding
import de.stenzel.tim.spieleabend.glide.GlideApp
import de.stenzel.tim.spieleabend.models.AssistantModel

class AssistantAdapter : RecyclerView.Adapter<AssistantAdapter.AssistantItemViewHolder>(){

    var onItemClick: ((AssistantModel) -> Unit)? = null
    private val data = ArrayList<AssistantModel>()

    fun setData(list: List<AssistantModel>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssistantItemViewHolder {
        val binding = AssistantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AssistantItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AssistantItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class AssistantItemViewHolder(private val binding: AssistantItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(data[absoluteAdapterPosition])
            }
        }

        fun bind(model: AssistantModel) {
            GlideApp.with(itemView).load(model.image).into(binding.assistantItemIv)
            binding.assistantItemTitle.text = model.title
        }

    }
}