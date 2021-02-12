package com.kuba.flashscore.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.LayoutDirection
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuba.flashscore.R
import com.kuba.flashscore.databinding.EventDetailItemBinding
import com.kuba.flashscore.local.models.INCIDENTTYPE
import com.kuba.flashscore.local.models.Incident
import java.util.*

const val ITEM_TYPE_NORMAL = 0
const val ITEM_TYPE_HEADER = 1

class EventDetailAdapter(private val context: Context) :
    RecyclerView.Adapter<EventDetailAdapter.EventDetailViewHolder>() {

    inner class EventDetailViewHolder(val binding: EventDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Objects>() {
        override fun areItemsTheSame(oldItem: Objects, newItem: Objects): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Objects, newItem: Objects): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var incidents: List<Objects>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDetailViewHolder {
        val binding = EventDetailItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return EventDetailViewHolder(binding)
    }

    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: EventDetailViewHolder, position: Int) {
        holder.binding.apply {
            val incident = incidents[position]
            if (!incident.whatTeam) constraintLayoutEventDetail.layoutDirection =
                LayoutDirection.RTL

            textViewEventDetailPlayerName.text = incident.firstName
            textViewEventDetailAssistName.text =
                if (incident.secondName.isNotEmpty()) "(${incident.secondName})" else ""
            textViewEventDetailMinutes.text = incident.time

            when (incident.INCIDENTTYPE) {
                INCIDENTTYPE.GOAL -> Glide.with(holder.itemView).load(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_football_ball
                    )
                ).into(imageViewEventDetail)
                INCIDENTTYPE.SUBSTITUTION -> Glide.with(holder.itemView).load(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_substitution
                    )
                ).into(imageViewEventDetail)
                INCIDENTTYPE.YELLOW_CARD -> Glide.with(holder.itemView).load(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_yellow_card
                    )
                ).into(imageViewEventDetail)
                else -> Glide.with(holder.itemView).load(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_red_card
                    )
                ).into(imageViewEventDetail)
            }
        }
    }

    override fun getItemCount(): Int = incidents.size


}
