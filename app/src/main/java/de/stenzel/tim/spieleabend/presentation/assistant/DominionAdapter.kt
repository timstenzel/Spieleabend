package de.stenzel.tim.spieleabend.presentation.assistant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import de.stenzel.tim.spieleabend.databinding.DominionChildItemBinding
import de.stenzel.tim.spieleabend.databinding.DominionExpandableParentItemBinding
import de.stenzel.tim.spieleabend.models.DominionModel
import de.stenzel.tim.spieleabend.models.ExpandableDominionCardModel
import de.stenzel.tim.spieleabend.models.ExpandableDominionExpansionModel
import java.lang.IllegalArgumentException

const val TYPE_PARENT = 1
const val TYPE_CHILD = 2

class DominionAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dominionList = ArrayList<Any>() //changes on rows being expanded/collapsed

    fun setData(listExpandable: List<Any>) {
        dominionList.clear()
        dominionList.addAll(listExpandable)
        notifyDataSetChanged()
    }

    fun getData() : ArrayList<Any> {
        return dominionList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_PARENT) {
                val binding = DominionExpandableParentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DominionParentItemViewHolder(binding)
            } else {
                val binding = DominionChildItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DominionChildItemViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return dominionList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DominionParentItemViewHolder) {
            holder.bind(dominionList[position] as ExpandableDominionExpansionModel, position)
        } else if (holder is DominionChildItemViewHolder) {
            holder.bind(dominionList[position] as ExpandableDominionCardModel, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (dominionList[position] is ExpandableDominionExpansionModel) {
            TYPE_PARENT
        } else {
            TYPE_CHILD
        }
    }

    fun isExpansion(itemPosition: Int) : Boolean {
        return dominionList[itemPosition] is ExpandableDominionExpansionModel
    }

    fun isCard(itemPosition: Int) : Boolean {
        return dominionList[itemPosition] is ExpandableDominionCardModel
    }

    private fun expandExpansion(position: Int) {
        if (isExpansion(position)) {
            val row = dominionList[position] as ExpandableDominionExpansionModel
            row.isExpanded = true
            val expansion = row.expansion
            val positionOfFirstCard = position + 1
            for (i in expansion.cards.indices) {
                if (isCard(positionOfFirstCard + i)) {
                    (dominionList[positionOfFirstCard + i] as ExpandableDominionCardModel).isExpanded = true
                }
            }
            notifyItemRangeChanged(position, expansion.cards.size + 1)
        }
    }

    private fun collapseExpansion(position: Int) {
        if (isExpansion(position)) {
            val row = dominionList[position] as ExpandableDominionExpansionModel
            row.isExpanded = false
            val expansion = row.expansion
            val positionOfFirstCard = position + 1
            for (i in expansion.cards.indices) {
                if (isCard(positionOfFirstCard + i)) { //extra check which should not be necessary
                    (dominionList[positionOfFirstCard + i] as ExpandableDominionCardModel).isExpanded = false
                }
            }
            notifyItemRangeChanged(position, expansion.cards.size + 1)
        }
    }

    inner class DominionParentItemViewHolder(private val binding: DominionExpandableParentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ExpandableDominionExpansionModel, position: Int) {
            binding.dominionExpansionTitle.text = model.expansion.title
            binding.dominionExpansionCb.isChecked = model.isSelected

            binding.dominionExpansionIndicator.setOnClickListener {
                val row = dominionList[position] as ExpandableDominionExpansionModel
                if (row.isExpanded) {
                    collapseExpansion(position)
                    //binding.dominionExpansionIndicator.animate().rotation(90f)
                } else {
                    expandExpansion(position)
                    //binding.dominionExpansionIndicator.animate().rotation(-90f)
                }
            }

            binding.dominionExpansionCb.setOnCheckedChangeListener { compoundButton, isChecked ->
                Toast.makeText(context, "Checkbox clicked -> checked: $isChecked", Toast.LENGTH_SHORT).show()
                val row = dominionList[position] as ExpandableDominionExpansionModel
                row.isSelected = isChecked

                val expansion = row.expansion
                val positionOfFirstCard = position + 1
                for (i in expansion.cards.indices) {
                    if (isCard(positionOfFirstCard + i)) { //extra check which should not be necessary
                        binding.dominionExpansionCb.isChecked = isChecked
                        val child = dominionList[positionOfFirstCard + i] as ExpandableDominionCardModel
                        child.isSelected = isChecked
                    }
                }
                notifyItemRangeChanged(positionOfFirstCard, expansion.cards.size)
            }
        }
    }

    inner class DominionChildItemViewHolder(private val binding: DominionChildItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ExpandableDominionCardModel, position: Int) {

            //trick to hide non expanded children
            val container = binding.dominionCardContainer
            if (model.isExpanded) {
                //show it
                container.visibility = View.VISIBLE
                container.layoutParams = ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
            } else {
                container.visibility = View.GONE
                container.layoutParams = ConstraintLayout.LayoutParams(0,0)
            }


            binding.dominionCardTitle.text = model.card.title
            binding.dominionCardCb.isChecked = model.isSelected

            binding.dominionCardCb.setOnCheckedChangeListener { compoundButton, isChecked ->
                //update selected state in dominionlist
                (dominionList[position] as ExpandableDominionCardModel).isSelected = isChecked
            }
        }
    }

}