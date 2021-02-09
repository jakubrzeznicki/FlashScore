package com.kuba.flashscore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kuba.flashscore.databinding.PlayerItemBinding
import com.kuba.flashscore.network.models.PlayerDto
import com.kuba.flashscore.ui.FlashScoreViewModel

class PlayersAdapter(private val context: Context) :
    RecyclerView.Adapter<PlayersAdapter.PlayersViewHolder>() {

    inner class PlayersViewHolder(val binding: PlayerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<PlayerDto>() {
        override fun areItemsTheSame(oldItem: PlayerDto, newItem: PlayerDto): Boolean {
            return oldItem.playerKey == newItem.playerKey
        }

        override fun areContentsTheSame(oldItem: PlayerDto, newItem: PlayerDto): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var players: List<PlayerDto>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder {
        val binding = PlayerItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {
        holder.binding.apply {
            val player = players[position]
            textViewPlayerName.text = player.playerName
            textViewPlayerNumber.text = player.playerNumber

            holder.itemView.setOnClickListener {
                Toast.makeText(context, "Clicked ${player.playerName}", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun getItemCount(): Int = players.size


}
