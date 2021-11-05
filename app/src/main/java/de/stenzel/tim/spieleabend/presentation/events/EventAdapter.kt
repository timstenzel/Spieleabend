package de.stenzel.tim.spieleabend.presentation.events

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.EventHeaderItemBinding
import de.stenzel.tim.spieleabend.databinding.EventItemBinding
import de.stenzel.tim.spieleabend.glide.GlideApp
import de.stenzel.tim.spieleabend.helpers.timestampToLocalDateTime
import de.stenzel.tim.spieleabend.models.remote.EventHeader
import de.stenzel.tim.spieleabend.models.remote.EventModel
import java.time.format.DateTimeFormatter
import javax.inject.Inject

const val TYPE_HEADER = 0
const val TYPE_EVENT = 1

class EventAdapter @Inject constructor(
    @ApplicationContext private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), StickyHeaderItemDecoration.StickyHeaderInterface {

    var onEventItemClick: ((EventModel) -> Unit)? = null
    private val data = ArrayList<Any>()
    private var userLat = -1.0
    private var userLon = -1.0

    fun setData(list: List<Any>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun setLocationData(latitude: Double, longitude: Double) {
        userLat = latitude
        userLon = longitude
        notifyDataSetChanged()
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return data[itemPosition] is EventHeader
    }

    override fun bindHeaderData(header: View, headerPosition: Int) {
        ((header as ConstraintLayout).getChildAt(0) as TextView).text = (data[headerPosition] as EventHeader).title
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
        return R.layout.event_header_item
    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var headerPosition = 0
        var position = itemPosition
        do {
            if (this.isHeader(position)) {
                headerPosition = position
                break
            }
            position -= 1
        } while (position >= 0)
        return headerPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val binding = EventHeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            EventHeaderItemViewHolder(binding)
        } else {
            val binding = EventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            EventItemViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EventHeaderItemViewHolder) {
            holder.bind(data[position] as EventHeader)
        } else if (holder is EventItemViewHolder) {
            holder.bind(data[position] as EventModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position] is EventHeader) {
            TYPE_HEADER
        } else {
            TYPE_EVENT
        }
    }

    inner class EventItemViewHolder(private val binding: EventItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private val storage = FirebaseStorage.getInstance()

        init {
            itemView.setOnClickListener {
                if (isHeader(absoluteAdapterPosition)) {
                    //don't do anything
                } else {
                    onEventItemClick?.invoke(data[absoluteAdapterPosition] as EventModel)
                }
            }
        }

        fun bind(model : EventModel) {
            val startDate = timestampToLocalDateTime(model.startDate!!)
            val endDate = timestampToLocalDateTime(model.endDate!!)
            val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

            if (model.img!!.isEmpty()) {
                GlideApp.with(itemView).load(R.drawable.event_default).into(binding.eventItemImage)
            } else {
                val ref = storage.getReferenceFromUrl(model.img)
                GlideApp.with(itemView).load(ref).error(R.drawable.error_default).into(binding.eventItemImage)
            }
            binding.eventItemTitle.text = model.title
            binding.eventItemDate.text = startDate.format(dateFormatter)
            binding.eventItemTime.text = context.getString(R.string.start_end_time, startDate.format(timeFormatter), endDate.format(timeFormatter))
            binding.eventItemLocation.text = model.location

            if (model.latitude != null && model.longitude != null && userLat != -1.0 && userLon != -1.0) {
                //val result = floatArrayOf()
                //Location.distanceBetween(userLat, userLon, model.latitude.toDouble(), model.longitude.toDouble(), result)
                val eventLocation = Location("event")
                eventLocation.latitude = model.latitude.toDouble()
                eventLocation.longitude = model.longitude.toDouble()

                val userLocation = Location("user")
                userLocation.latitude = userLat
                userLocation.longitude = userLon

                val distanceM = eventLocation.distanceTo(userLocation).toInt()
                //val distanceM = result[0].toInt()
                if (distanceM < 1000) {
                    model.distance = context.getString(R.string.distance_m, distanceM)
                    binding.eventItemDistance.visibility = View.VISIBLE
                    binding.eventItemDistance.text = context.getString(R.string.distance_m, distanceM)
                } else {
                    val distanceKm = distanceM / 1000
                    model.distance = context.getString(R.string.distance_km, distanceKm)
                    binding.eventItemDistance.visibility = View.VISIBLE
                    binding.eventItemDistance.text = context.getString(R.string.distance_km, distanceKm)
                }
            } else {
                binding.eventItemDistance.visibility = View.GONE
            }
        }
    }

    inner class EventHeaderItemViewHolder(private val binding: EventHeaderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model : EventHeader) {
            binding.eventItemHeaderTv.text = model.title
        }
    }









}