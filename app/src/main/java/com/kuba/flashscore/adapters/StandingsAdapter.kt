package com.kuba.flashscore.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuba.flashscore.R
import com.kuba.flashscore.databinding.StandingsItemBinding
import com.kuba.flashscore.network.models.LeagueDto
import com.kuba.flashscore.network.models.StandingDto
import com.kuba.flashscore.ui.teams.TeamsViewPagerFragmentDirections

class StandingsAdapter(
    private val context: Context,
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

            if (whichStandings == "overall" && standing.overallPromotion.isNotEmpty()) {
                setColorDependingOnThePosition(textViewTeamPosition, standing, position)
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


    private fun setColorDependingOnThePosition(positionTextView: TextView, standing: StandingDto, position: Int) {
        if ((standing.overallPromotion == "Promotion - Premier League" || standing.overallPromotion == "Promotion - Ligue 1") && (position == 0 || position == 1)) {
            positionTextView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.directPromotionColor
                )
            )
        }
        else if (standing.overallPromotion == "Promotion - Championship (Play Offs)") {
            positionTextView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.playOffsColor
                )
            )

        }
        else if (standing.overallPromotion == "Promotion - Ligue 1 (Promotion)") {
            positionTextView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.promotionColor
                )
            )
        }
        else if (standing.overallPromotion == "Ligue 2 (Relegation)") {
            positionTextView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.playOffKeepingColor
                )
            )
        }
        else if (standing.overallPromotion == "Relegation") {
            positionTextView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.relegationColor
                )
            )
        }

    }

    override fun getItemCount(): Int = standings.size

}
