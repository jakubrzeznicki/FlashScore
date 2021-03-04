package com.kuba.flashscore.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuba.flashscore.R
import com.kuba.flashscore.databinding.EventItemBinding
import com.kuba.flashscore.local.models.entities.event.CountryWithLeagueWithEventsAndTeams
import com.kuba.flashscore.local.models.entities.event.EventEntity
import com.kuba.flashscore.other.Constants.MATCH_STATUS_FINISHED
import com.kuba.flashscore.other.Constants.MATCH_STATUS_ZERO
import com.kuba.flashscore.ui.events.EventsListFragmentDirections

class EventAdapter :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(val binding: EventItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback =
        object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem.matchId == newItem.matchId
            }

            override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }
        }

    private val differ = AsyncListDiffer(this, diffCallback)

    var events: List<EventEntity?>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    lateinit var countryWithLeagueWithEventsAndTeams: CountryWithLeagueWithEventsAndTeams
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = EventItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.binding.apply {
            val event = events[position]
            val homeTeam =
                countryWithLeagueWithEventsAndTeams.leagueWithTeams[0].teams.filter { teamEntity ->
                    teamEntity.teamKey == event?.matchHometeamId
                }
            val awayTeam =
                countryWithLeagueWithEventsAndTeams.leagueWithTeams[0].teams.filter { teamEntity ->
                    teamEntity.teamKey == event?.matchAwayteamId
                }
            Glide.with(holder.itemView).load(homeTeam[0].teamBadge).into(imageViewFirstTeamLogo)
            Glide.with(holder.itemView).load(awayTeam[0].teamBadge).into(imageViewSecondTeamLogo)
            textViewFirstTeamName.text = homeTeam[0].teamName
            textViewSecondTeamName.text = awayTeam[0].teamName

            if (event?.matchLive == MATCH_STATUS_ZERO) {
                textViewEventMinutes.visibility = View.INVISIBLE
                if (event.matchStatus.isNotEmpty() && event.matchStatus == MATCH_STATUS_FINISHED) {
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
                    text = event?.matchStatus
                    setTextColor(resources.getColor(R.color.error))
                }
                textViewEventHour.visibility = View.INVISIBLE
                textViewEventGoalsFirstTeam.apply {
                    visibility = View.VISIBLE
                    text = "${event?.matchHometeamScore}'"
                    setTextColor(resources.getColor(R.color.error))
                }
                textViewEventGoalSecondTeam.apply {
                    visibility = View.VISIBLE
                    text = "${event?.matchAwayteamScore}'"
                    setTextColor(resources.getColor(R.color.error))
                }

            }

            holder.itemView.setOnClickListener {
                val action =
                    EventsListFragmentDirections.actionEventsListFragmentToEventDetailsViewPagerFragment(
                        countryWithLeagueWithEventsAndTeams,
                        event?.matchId!!
                    )
                it.findNavController().navigate(action)
            }
        }
    }


    override fun getItemCount(): Int = events.size


}
