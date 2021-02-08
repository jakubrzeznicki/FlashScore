package com.kuba.flashscore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuba.flashscore.data.local.entities.Country
import com.kuba.flashscore.data.local.entities.Team

import com.kuba.flashscore.databinding.CountryItemBinding
import com.kuba.flashscore.databinding.TeamItemBinding
import com.kuba.flashscore.ui.FlashScoreViewModel
import com.kuba.flashscore.ui.country.CountryFragmentDirections
import com.kuba.flashscore.ui.teams.TeamsViewPagerFragmentDirections

class TeamsAdapter(private val context: Context, private val viewModel: FlashScoreViewModel) :
    RecyclerView.Adapter<TeamsAdapter.TeamViewHolder>() {

    inner class TeamViewHolder(val binding: TeamItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Team>() {
        override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem.teamKey == newItem.teamKey
        }

        override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var teams: List<Team>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val binding = TeamItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.binding.apply {
            val team = teams[position]
            Glide.with(holder.itemView).load(team.teamBadge).into(imageViewTeamLogo)
            textViewTeamName.text = team.teamName

            holder.itemView.setOnClickListener {
                val action =
                    TeamsViewPagerFragmentDirections.actionTeamsViewPagerFragmentToClubViewPagerFragment(null, team.teamKey)
                it.findNavController().navigate(action)
            }
        }
    }


    override fun getItemCount(): Int = teams.size


}
