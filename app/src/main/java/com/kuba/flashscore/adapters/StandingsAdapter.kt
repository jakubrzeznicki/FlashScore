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
import com.kuba.flashscore.data.domain.models.Standing
import com.kuba.flashscore.data.domain.models.customs.CountryWithLeagueAndTeams
import com.kuba.flashscore.databinding.StandingsItemBinding
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.StandingEntity
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
            val team = countryWithLeagueAndTeams.leagueWithTeams[0].teams.firstOrNull { it.teamKey == standing.teamId }
            Glide.with(holder.itemView).load(team?.teamBadge).into(imageViewTeamLogo)
            textViewTeamName.text = team?.teamName
            textViewTeamBalance.text = "${standing.leagueGF} - ${standing.leagueGA}"

            textViewTeamPlayedMatch.text = standing.leaguePayed

            textViewTeamPoints.text = standing.leaguePTS

            textViewTeamPosition.text = standing.leaguePosition


            if (whichStandings == OVERALL_LEAGUE && standing.promotion.isNotEmpty()) {
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
        standing: Standing,
        position: Int
    ) {
        if ((standing.promotion == LEAGUE_PROMOTION_PREMIER_LEAGUE || standing.promotion == LEAGUE_PROMOTION_LIGUE_1) && (position == 0 || position == 1)) {
            positionTextView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.directPromotionColor
                )
            )
        } else if (standing.promotion == LEAGUE_PROMOTION_CHAMPIONSHIP_PLAY_OFFS) {
            positionTextView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.playOffsColor
                )
            )

        } else if (standing.promotion == LEAGUE_PROMOTION_LEAGUE_1_PROMOTION) {
            positionTextView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.promotionColor
                )
            )
        } else if (standing.promotion == LEAGUE_RELEGATION_LIGUE_2) {
            positionTextView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.playOffKeepingColor
                )
            )
        } else if (standing.promotion == LEAGUE_RELEGATION) {
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
