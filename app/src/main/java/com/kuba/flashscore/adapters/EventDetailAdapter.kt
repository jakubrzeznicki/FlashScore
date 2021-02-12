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
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.kuba.flashscore.R
import com.kuba.flashscore.databinding.EventDetailItemBinding
import com.kuba.flashscore.databinding.EventDetailHeaderBinding
import com.kuba.flashscore.local.models.INCIDENTTYPE
import com.kuba.flashscore.local.models.Incident
import com.kuba.flashscore.local.models.IncidentHeader
import com.kuba.flashscore.local.models.IncidentItem
import com.kuba.flashscore.other.Constants.ITEM_TYPE_HEADER
import com.kuba.flashscore.other.Constants.ITEM_TYPE_NORMAL


class EventDetailAdapter(private val context: Context) :
    RecyclerView.Adapter<EventDetailAdapter.EventDetailViewHolder>() {

    inner class EventDetailViewHolder(val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Incident>() {
        override fun areItemsTheSame(oldItem: Incident, newItem: Incident): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Incident, newItem: Incident): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var incidents: List<Incident>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDetailViewHolder {
        val binding = if (viewType == ITEM_TYPE_NORMAL) EventDetailItemBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ) else EventDetailHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventDetailViewHolder(binding)
    }

    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: EventDetailViewHolder, position: Int) {
        val itemType = getItemViewType(position)

        if (itemType == ITEM_TYPE_NORMAL) {
            holder.binding as EventDetailItemBinding
            holder.binding.apply {
                val incident = incidents[position] as IncidentItem
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
        } else {
            holder.binding as EventDetailHeaderBinding
            holder.binding.apply {
                val incident = incidents[position] as IncidentHeader
                textViewInformationAboutFirstHalf.text = incident.name
                textViewInformationAboutFirstHalfResult.text = incident.result
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (incidents[position] is IncidentHeader) {
            ITEM_TYPE_HEADER;
        } else {
            ITEM_TYPE_NORMAL;
        }
    }

    override fun getItemCount(): Int = incidents.size

}
