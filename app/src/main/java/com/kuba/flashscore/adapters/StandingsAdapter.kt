package com.kuba.flashscore.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuba.flashscore.databinding.StandingsItemBinding
import com.kuba.flashscore.network.models.LeagueDto
import com.kuba.flashscore.network.models.StandingDto
import com.kuba.flashscore.ui.teams.TeamsViewPagerFragmentDirections

class StandingsAdapter(
    private val league: LeagueDto,
    private val whichStandings: String
) :
    RecyclerView.Adapter<StandingsAdapter.StandingsViewHolder>() {


    inner class StandingsViewHolder(val binding: StandingsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<StandingDto>() {
        override fun areItemsTheSame(oldItem: StandingDto, newItem: StandingDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: StandingDto, newItem: StandingDto): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var standings: List<StandingDto>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StandingsViewHolder {
        val binding = StandingsItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return StandingsViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StandingsViewHolder, position: Int) {
        holder.binding.apply {
            val standing = standings[position]
            Glide.with(holder.itemView).load(standing.teamBadge).into(imageViewTeamLogo)
            textViewTeamName.text = standing.teamName
            textViewTeamBalance.text = when (whichStandings) {
                "overall" -> "${standing.overallLeagueGF} - ${standing.overallLeagueGA}"
                "home" -> "${standing.homeLeagueGF} - ${standing.homeLeagueGA}"
                else -> "${standing.awayLeagueGF} - ${standing.awayLeagueGA}"
            }
            textViewTeamPlayedMatch.text =
                when (whichStandings) {
                    "overall" -> standing.overallLeaguePayed
                    "home" -> standing.homeLeaguePayed
                    else -> standing.awayLeaguePayed
                }
            textViewTeamPoints.text = when (whichStandings) {
                "overall" -> standing.overallLeaguePTS
                "home" -> standing.homeLeaguePTS
                else -> standing.awayLeaguePTS
            }
            textViewTeamPosition.text = when (whichStandings) {
                "overall" -> standing.overallLeaguePosition
                "home" -> standing.homeLeaguePosition
                else -> standing.awayLeaguePosition
            }

            holder.itemView.setOnClickListener {
                val action =
                    TeamsViewPagerFragmentDirections.actionTeamsViewPagerFragmentToClubViewPagerFragment(
                        standing.teamId,
                        league,
                        standing.teamName,
                        standing.teamBadge
                    )
                it.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int = standings.size

}
