package com.kuba.flashscore.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuba.flashscore.data.local.entities.Country
import com.kuba.flashscore.data.local.entities.League
import com.kuba.flashscore.data.local.entities.Standing
import com.kuba.flashscore.data.local.entities.Team

import com.kuba.flashscore.databinding.CountryItemBinding
import com.kuba.flashscore.databinding.StandingsItemBinding
import com.kuba.flashscore.databinding.TeamItemBinding
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.ui.FlashScoreViewModel
import com.kuba.flashscore.ui.country.CountryFragmentDirections
import com.kuba.flashscore.ui.teams.TeamsViewPagerFragmentDirections
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class StandingsAdapter(
    private val context: Context,
    private val league: League,
    private val whichStandings: String
) :
    RecyclerView.Adapter<StandingsAdapter.StandingsViewHolder>() {


    inner class StandingsViewHolder(val binding: StandingsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Standing>() {
        override fun areItemsTheSame(oldItem: Standing, newItem: Standing): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Standing, newItem: Standing): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var standings: List<Standing>
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
