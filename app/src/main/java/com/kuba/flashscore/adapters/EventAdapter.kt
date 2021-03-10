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
import com.kuba.flashscore.data.domain.models.event.Event
import com.kuba.flashscore.data.domain.models.event.customs.CountryWithLeagueWithEventsAndTeams
import com.kuba.flashscore.data.domain.models.event.customs.EventWithEventInformation
import com.kuba.flashscore.databinding.EventItemBinding
import com.kuba.flashscore.data.local.models.entities.event.customs.CountryWithLeagueWithEventsAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.EventEntity
import com.kuba.flashscore.other.Constants.MATCH_STATUS_FINISHED
import com.kuba.flashscore.other.Constants.MATCH_STATUS_ZERO
import com.kuba.flashscore.ui.events.EventsListFragmentDirections
import timber.log.Timber

class EventAdapter :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(val binding: EventItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback =
        object : DiffUtil.ItemCallback<EventWithEventInformation>() {
            override fun areItemsTheSame(
                oldItem: EventWithEventInformation,
                newItem: EventWithEventInformation
            ): Boolean {
                return oldItem.event.matchId == newItem.event.matchId
            }

            override fun areContentsTheSame(
                oldItem: EventWithEventInformation,
                newItem: EventWithEventInformation
            ): Boolean {
                return oldItem == newItem
            }
        }

    private val differ = AsyncListDiffer(this, diffCallback)

    var events: List<EventWithEventInformation?>
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
                countryWithLeagueWithEventsAndTeams.leagueWithTeams[0].teams.filter { team ->
                    team.teamKey == event?.eventInformation?.get(0)?.teamId
                }
            Timber.d("EVENT DETAIL ADAPET ${homeTeam}, ${countryWithLeagueWithEventsAndTeams.leagueWithTeams[0].teams.size},  ${event?.eventInformation?.get(0)?.teamId} ")
            val awayTeam =
                countryWithLeagueWithEventsAndTeams.leagueWithTeams[0].teams.filter { team ->
                    team.teamKey == event?.eventInformation?.get(1)?.teamId
                }
            Timber.d("EVENT DETAIL ADAPET ${awayTeam}, ${countryWithLeagueWithEventsAndTeams.leagueWithTeams[0].teams.size},  ${event?.eventInformation?.get(1)?.teamId} ")

            Glide.with(holder.itemView).load(homeTeam[0].teamBadge).into(imageViewFirstTeamLogo)
            Glide.with(holder.itemView).load(awayTeam[0].teamBadge).into(imageViewSecondTeamLogo)
            textViewFirstTeamName.text = homeTeam[0].teamName
            textViewSecondTeamName.text = awayTeam[0].teamName

            if (event?.event?.matchLive == MATCH_STATUS_ZERO) {
                textViewEventMinutes.visibility = View.INVISIBLE
                if (event.event.matchStatus.isNotEmpty() && event.event.matchStatus == MATCH_STATUS_FINISHED) {
                    textViewEventHour.visibility = View.INVISIBLE
                    textViewEventGoalsFirstTeam.apply {
                        visibility = View.VISIBLE
                        text = event.eventInformation[0].teamScore
                    }
                    textViewEventGoalSecondTeam.apply {
                        visibility = View.VISIBLE
                        text = event.eventInformation[1].teamScore
                    }
                } else {
                    textViewEventHour.apply {
                        visibility = View.VISIBLE
                        text = event.event.matchTime
                    }
                    textViewEventGoalsFirstTeam.visibility = View.INVISIBLE
                    textViewEventGoalSecondTeam.visibility = View.INVISIBLE
                }
            } else {
                textViewEventMinutes.apply {
                    visibility = View.VISIBLE
                    text = event?.event?.matchStatus
                    setTextColor(resources.getColor(R.color.error))
                }
                textViewEventHour.visibility = View.INVISIBLE
                textViewEventGoalsFirstTeam.apply {
                    visibility = View.VISIBLE
                    text = "${event?.eventInformation?.get(0)?.teamScore}'"
                    setTextColor(resources.getColor(R.color.error))
                }
                textViewEventGoalSecondTeam.apply {
                    visibility = View.VISIBLE
                    text = "${event?.eventInformation?.get(1)?.teamScore}'"
                    setTextColor(resources.getColor(R.color.error))
                }

            }

            holder.itemView.setOnClickListener {
                val action =
                    EventsListFragmentDirections.actionEventsListFragmentToEventDetailsViewPagerFragment(
                        event?.event?.matchId!!,
                        countryWithLeagueWithEventsAndTeams
                    )
                it.findNavController().navigate(action)
            }
        }
    }


    override fun getItemCount(): Int = events.size


}
