package de.stenzel.tim.spieleabend.presentation.assistant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.DominionChildItemBinding
import de.stenzel.tim.spieleabend.databinding.DominionExpandableParentItemBinding
import de.stenzel.tim.spieleabend.models.local.DominionCard
import de.stenzel.tim.spieleabend.models.local.DominionExpansion
import javax.inject.Inject

const val TYPE_PARENT = 1
const val TYPE_CHILD = 2

class DominionAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dominionList = ArrayList<Any>()
    private val expansions = ArrayList<DominionExpansion>()
    private val cards = ArrayList<DominionCard>()
    private val selectedCardIds = ArrayList<Int>()
    private val selectedExpansionIds = ArrayList<Int>()
    private val expandedExpansionIds = ArrayList<Int>()

    fun setData(expandableList: List<Any>) {
        dominionList.clear()
        dominionList.addAll(expandableList)
        expansions.clear()
        cards.clear()

        for (item in dominionList) {
            if (item is DominionExpansion) {
                expansions.add(item)
                selectedExpansionIds.add(item.id)
            } else if (item is DominionCard){
                cards.add(item)
                selectedCardIds.add(item.id)
            }
        }

        notifyDataSetChanged()
    }

    fun getData() : ArrayList<Any> {
        return dominionList
    }

    fun getSelectedCards() : ArrayList<Int> {
        return selectedCardIds
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
            holder.bind(dominionList[position] as DominionExpansion, position)
        } else if (holder is DominionChildItemViewHolder) {
            holder.bind(dominionList[position] as DominionCard, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (dominionList[position] is DominionExpansion) {
            TYPE_PARENT
        } else {
            TYPE_CHILD
        }
    }

    fun isExpansion(itemPosition: Int) : Boolean {
        return dominionList[itemPosition] is DominionExpansion
    }

    fun isCard(itemPosition: Int) : Boolean {
        return dominionList[itemPosition] is DominionCard
    }

    fun isExpansionExpanded(id: Int) : Boolean {
        return expandedExpansionIds.contains(id)
    }

    fun isCardChecked(id: Int) : Boolean {
        return selectedCardIds.contains(id)
    }

    inner class DominionParentItemViewHolder(private val binding: DominionExpandableParentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: DominionExpansion, position: Int) {
            binding.dominionExpansionTitle.text = model.title
            binding.dominionExpansionCb.isChecked = selectedExpansionIds.contains(model.id)

            binding.dominionExpansionIndicator.setOnClickListener {
                val row = dominionList[position] as DominionExpansion
                if (isExpansionExpanded(row.id)) {
                    //currently expanded -> collapse it
                    expandedExpansionIds.remove(row.id)
                    binding.dominionExpansionIndicator.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
                } else {
                    //currently collapsed -> expand it
                    expandedExpansionIds.add(row.id)
                    binding.dominionExpansionIndicator.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                }
                notifyDataSetChanged()
            }

            binding.dominionExpansionCb.setOnCheckedChangeListener { compoundButton, isChecked ->
                val row = dominionList[position] as DominionExpansion

                if (isChecked) {
                    //select expansion
                    selectedExpansionIds.add(row.id)
                    //also select all cards of this expansion
                    for (card in cards) {
                        if (card.expansionId == row.id) {
                            if (!selectedCardIds.contains(card.id)) {
                                selectedCardIds.add(card.id)
                            }
                        }
                    }

                } else {
                    //unselect expansion
                    selectedExpansionIds.remove(row.id)
                    //also unselect all cards of this expansion
                    for (card in cards) {
                        if (card.expansionId == row.id) {
                            if (selectedCardIds.contains(card.id)) {
                                selectedCardIds.remove(card.id)
                            }
                        }
                    }
                }
                if (isExpansionExpanded(row.id)) {
                    notifyDataSetChanged()
                }
            }
        }
    }

    inner class DominionChildItemViewHolder(private val binding: DominionChildItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: DominionCard, position: Int) {

            //trick to hide non expanded children
            val container = binding.dominionCardContainer
            if (isExpansionExpanded(model.expansionId)) {
                //show it
                container.visibility = View.VISIBLE
                container.layoutParams = ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
            } else {
                container.visibility = View.GONE
                container.layoutParams = ConstraintLayout.LayoutParams(0,0)
            }

            binding.dominionCardCb.setOnCheckedChangeListener { compoundButton, b ->
                //this is done to remove the current listener
                //since calling .isChecked triggers the listener
                //after calling .isChecked the listener is set again
            }

            binding.dominionCardTitle.text = model.title
            binding.dominionCardCb.isChecked = isCardChecked(model.id)

            binding.dominionCardCb.setOnCheckedChangeListener { compoundButton, isChecked ->
                //update selected state
                if (isChecked) {
                    selectedCardIds.add(model.id)
                } else {
                    selectedCardIds.remove(model.id)
                }
            }
        }
    }

}