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
    private val viewModel: FlashScoreViewModel,
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

        when (whichStandings) {
            "overall" -> setupOverall(holder, position)
            "away" -> setupAway(holder, position)
            else -> setupHome(holder, position)
        }

        val standing = standings[position]
        holder.binding.apply {
            holder.itemView.setOnClickListener {
                val action =
                    TeamsViewPagerFragmentDirections.actionTeamsViewPagerFragmentToClubViewPagerFragment(
                        null,
                        standing.teamId
                    )
                it.findNavController().navigate(action)
            }
        }
    }

    private fun setupOverall(holder: StandingsViewHolder, position: Int) {
        holder.binding.apply {
            val standing = standings[position]
            Glide.with(holder.itemView).load(standing.teamBadge).into(imageViewTeamLogo)
            textViewTeamName.text = standing.teamName
            textViewTeamBalance.text = "${standing.overallLeagueGF} - ${standing.overallLeagueGA}"
            textViewTeamPlayedMatch.text = standing.overallLeaguePayed
            textViewTeamPoints.text = standing.overallLeaguePTS
            textViewTeamPosition.text = standing.overallLeaguePosition
        }
    }

    private fun setupAway(holder: StandingsViewHolder, position: Int) {
        holder.binding.apply {
            val standing = standings[position]
            Glide.with(holder.itemView).load(standing.teamBadge).into(imageViewTeamLogo)
            textViewTeamName.text = standing.teamName
            textViewTeamBalance.text = "${standing.awayLeagueGF} - ${standing.awayLeagueGA}"
            textViewTeamPlayedMatch.text = standing.awayLeaguePayed
            textViewTeamPoints.text = standing.awayLeaguePTS
            textViewTeamPosition.text = standing.awayLeaguePosition
        }
    }

    private fun setupHome(holder: StandingsViewHolder, position: Int) {
        holder.binding.apply {
            val standing = standings[position]
            Glide.with(holder.itemView).load(standing.teamBadge).into(imageViewTeamLogo)
            textViewTeamName.text = standing.teamName
            textViewTeamBalance.text = "${standing.homeLeagueGF} - ${standing.homeLeagueGA}"
            textViewTeamPlayedMatch.text = standing.homeLeaguePayed
            textViewTeamPoints.text = standing.homeLeaguePTS
            textViewTeamPosition.text = standing.homeLeaguePosition
        }
    }

    override fun getItemCount(): Int = standings.size

}
