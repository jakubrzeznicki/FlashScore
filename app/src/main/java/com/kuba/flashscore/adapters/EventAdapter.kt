package com.kuba.flashscore.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuba.flashscore.R
import com.kuba.flashscore.databinding.EventItemBinding
import com.kuba.flashscore.databinding.LeagueItemBinding
import com.kuba.flashscore.network.models.LeagueDto
import com.kuba.flashscore.network.models.events.EventDto
import com.kuba.flashscore.ui.FlashScoreViewModel
import com.kuba.flashscore.ui.events.EventsListFragmentDirections
import com.kuba.flashscore.ui.league.LeagueFragmentDirections
import timber.log.Timber

class EventAdapter :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(val binding: EventItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<EventDto>() {
        override fun areItemsTheSame(oldItem: EventDto, newItem: EventDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: EventDto, newItem: EventDto): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var events: List<EventDto>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = EventItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.binding.apply {
            val event = events[position]
            Glide.with(holder.itemView).load(event.teamHomeBadge).into(imageViewFirstTeamLogo)
            Glide.with(holder.itemView).load(event.teamAwayBadge).into(imageViewSecondTeamLogo)
            textViewFirstTeamName.text = event.matchHometeamName
            textViewSecondTeamName.text = event.matchAwayteamName

            if (event.matchLive == "0") {
                textViewEventMinutes.visibility = View.INVISIBLE
                if (event.matchStatus.isNotEmpty() && event.matchStatus == "Finished") {
                    textViewEventHour.visibility = View.INVISIBLE
                    textViewEventGoalsFirstTeam.apply {
                        visibility = View.VISIBLE
                        text = event.matchHometeamScore
                    }
                    textViewEventGoalSecondTeam.apply {
                        visibility = View.VISIBLE
                        text = event.matchAwayteamScore
                    }
                } else {
                    textViewEventHour.apply {
                        visibility = View.VISIBLE
                        text = event.matchTime
                    }
                    textViewEventGoalsFirstTeam.visibility = View.INVISIBLE
                    textViewEventGoalSecondTeam.visibility = View.INVISIBLE
                }
            } else {
                textViewEventMinutes.apply {
                    visibility = View.VISIBLE
                    text = event.matchStatus
                    setTextColor(resources.getColor(R.color.error))
                }
                textViewEventHour.visibility = View.INVISIBLE
                textViewEventGoalsFirstTeam.apply {
                    visibility = View.VISIBLE
                    text = "${event.matchHometeamScore}'"
                    setTextColor(resources.getColor(R.color.error))
                }
                textViewEventGoalSecondTeam.apply {
                    visibility = View.VISIBLE
                    text = "${event.matchAwayteamScore}'"
                    setTextColor(resources.getColor(R.color.error))
                }

            }

            holder.itemView.setOnClickListener {
                val action =
                    EventsListFragmentDirections.actionEventsListFragmentToEventDetailsViewPagerFragment(
                        event
                    )
                it.findNavController().navigate(action)
            }
        }
    }


    override fun getItemCount(): Int = events.size


}
