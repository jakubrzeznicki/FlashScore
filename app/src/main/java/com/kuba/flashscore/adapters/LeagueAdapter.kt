package com.kuba.flashscore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuba.flashscore.databinding.LeagueItemBinding
import com.kuba.flashscore.network.models.LeagueDto
import com.kuba.flashscore.ui.FlashScoreViewModel
import com.kuba.flashscore.ui.league.LeagueFragmentDirections

class LeagueAdapter() :
    RecyclerView.Adapter<LeagueAdapter.LeagueViewHolder>() {

    inner class LeagueViewHolder(val binding: LeagueItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<LeagueDto>() {
        override fun areItemsTheSame(oldItem: LeagueDto, newItem: LeagueDto): Boolean {
            return oldItem.leagueId == newItem.leagueId
        }

        override fun areContentsTheSame(oldItem: LeagueDto, newItem: LeagueDto): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var league: List<LeagueDto>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val binding = LeagueItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return LeagueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.binding.apply {
            val league = league[position]
            Glide.with(holder.itemView).load(league.leagueLogo).into(imageViewLeagueLogo)
            textViewLeagueName.text = league.leagueName
            textViewLeagueSeason.text = league.leagueSeason

            holder.itemView.setOnClickListener {
                val action =
                    LeagueFragmentDirections.actionLeagueFragmentToTeamsViewPagerFragment(
                        league
                    )
                it.findNavController().navigate(action)
            }
        }
    }


    override fun getItemCount(): Int = league.size


}
