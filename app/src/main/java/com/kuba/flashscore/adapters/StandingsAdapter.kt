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
import com.kuba.flashscore.local.models.entities.CountryAndLeagues
import com.kuba.flashscore.local.models.entities.CountryWithLeagueAndTeams
import com.kuba.flashscore.local.models.entities.StandingEntity
import com.kuba.flashscore.local.models.entities.TeamEntity
import com.kuba.flashscore.network.models.StandingDto
import com.kuba.flashscore.other.Constants.HOME_LEAGUE
import com.kuba.flashscore.other.Constants.LEAGUE_PROMOTION_CHAMPIONSHIP_PLAY_OFFS
import com.kuba.flashscore.other.Constants.LEAGUE_PROMOTION_LEAGUE_1_PROMOTION
import com.kuba.flashscore.other.Constants.LEAGUE_PROMOTION_LIGUE_1
import com.kuba.flashscore.other.Constants.LEAGUE_PROMOTION_PREMIER_LEAGUE
import com.kuba.flashscore.other.Constants.LEAGUE_RELEGATION
import com.kuba.flashscore.other.Constants.LEAGUE_RELEGATION_LIGUE_2
import com.kuba.flashscore.other.Constants.OVERALL_LEAGUE
import com.kuba.flashscore.ui.teams.TeamsViewPagerFragmentDirections

class StandingsAdapter(
    private val context: Context,
    private val countryWithLeagueAndTeams: CountryWithLeagueAndTeams,
    private val whichStandings: String
) :
    RecyclerView.Adapter<StandingsAdapter.StandingsViewHolder>() {


    inner class StandingsViewHolder(val binding: StandingsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<StandingEntity>() {
        override fun areItemsTheSame(oldItem: StandingEntity, newItem: StandingEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: StandingEntity, newItem: StandingEntity): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var standings: List<StandingEntity>
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
            val team = countryWithLeagueAndTeams.leagueWithTeams[0].teams.firstOrNull { it.teamKey == standing.teamId }
            Glide.with(holder.itemView).load(team?.teamBadge).into(imageViewTeamLogo)
            textViewTeamName.text = team?.teamName
            textViewTeamBalance.text = when (whichStandings) {
                OVERALL_LEAGUE -> "${standing.overallLeagueGF} - ${standing.overallLeagueGA}"
                HOME_LEAGUE -> "${standing.homeLeagueGF} - ${standing.homeLeagueGA}"
                else -> "${standing.awayLeagueGF} - ${standing.awayLeagueGA}"
            }
            textViewTeamPlayedMatch.text =
                when (whichStandings) {
                    OVERALL_LEAGUE -> standing.overallLeaguePayed
                    HOME_LEAGUE -> standing.homeLeaguePayed
                    else -> standing.awayLeaguePayed
                }
            textViewTeamPoints.text = when (whichStandings) {
                OVERALL_LEAGUE -> standing.overallLeaguePTS
                HOME_LEAGUE -> standing.homeLeaguePTS
                else -> standing.awayLeaguePTS
            }
            textViewTeamPosition.text = when (whichStandings) {
                OVERALL_LEAGUE -> standing.overallLeaguePosition
                HOME_LEAGUE -> standing.homeLeaguePosition
                else -> standing.awayLeaguePosition
            }

            if (whichStandings == OVERALL_LEAGUE && standing.overallPromotion.isNotEmpty()) {
                setColorDependingOnThePosition(textViewTeamPosition, standing, position)
            }

            holder.itemView.setOnClickListener {
                val action =
                    TeamsViewPagerFragmentDirections.actionTeamsViewPagerFragmentToClubViewPagerFragment(
                        standing.teamId,
                        countryWithLeagueAndTeams
                    )
                it.findNavController().navigate(action)
            }
        }
    }


    private fun setColorDependingOnThePosition(
        positionTextView: TextView,
        standing: StandingEntity,
        position: Int
    ) {
        if ((standing.overallPromotion == LEAGUE_PROMOTION_PREMIER_LEAGUE || standing.overallPromotion == LEAGUE_PROMOTION_LIGUE_1) && (position == 0 || position == 1)) {
            positionTextView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.directPromotionColor
                )
            )
        } else if (standing.overallPromotion == LEAGUE_PROMOTION_CHAMPIONSHIP_PLAY_OFFS) {
            positionTextView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.playOffsColor
                )
            )

        } else if (standing.overallPromotion == LEAGUE_PROMOTION_LEAGUE_1_PROMOTION) {
            positionTextView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.promotionColor
                )
            )
        } else if (standing.overallPromotion == LEAGUE_RELEGATION_LIGUE_2) {
            positionTextView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.playOffKeepingColor
                )
            )
        } else if (standing.overallPromotion == LEAGUE_RELEGATION) {
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
