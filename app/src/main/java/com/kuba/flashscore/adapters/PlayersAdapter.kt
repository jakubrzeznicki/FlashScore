package com.kuba.flashscore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kuba.flashscore.databinding.PlayerItemBinding
import com.kuba.flashscore.local.models.entities.PlayerEntity
import com.kuba.flashscore.ui.teams.club.ClubViewPagerFragmentDirections
import timber.log.Timber

class PlayersAdapter(
    private val teamName: String,
    private val teamLogo: String
) :
    RecyclerView.Adapter<PlayersAdapter.PlayersViewHolder>() {

    inner class PlayersViewHolder(val binding: PlayerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<PlayerEntity>() {
        override fun areItemsTheSame(oldItem: PlayerEntity, newItem: PlayerEntity): Boolean {
            return oldItem.playerKey == newItem.playerKey
        }

        override fun areContentsTheSame(oldItem: PlayerEntity, newItem: PlayerEntity): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var players: List<PlayerEntity>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder {
        val binding = PlayerItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {
        Timber.d("DEJZI2 ${players}")
        Timber.d("DEJZI2 ${players.size}")
        holder.binding.apply {
            val player = players[position]
            textViewPlayerName.text = player.playerName
            textViewPlayerNumber.text = player.playerNumber
            holder.itemView.setOnClickListener {
                val action =
                    ClubViewPagerFragmentDirections.actionClubViewPagerFragmentToPlayerViewPagerFragment(
                        teamName,
                        player,
                        teamLogo
                    )
                it.findNavController().navigate(action)
            }
        }
    }


    override fun getItemCount(): Int = players.size


}
