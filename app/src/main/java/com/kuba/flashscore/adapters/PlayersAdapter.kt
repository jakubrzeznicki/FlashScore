package com.kuba.flashscore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuba.flashscore.data.local.entities.Country
import com.kuba.flashscore.data.local.entities.Player

import com.kuba.flashscore.databinding.CountryItemBinding
import com.kuba.flashscore.databinding.PlayerItemBinding
import com.kuba.flashscore.ui.FlashScoreViewModel
import com.kuba.flashscore.ui.country.CountryFragmentDirections

class PlayersAdapter(private val context: Context, private val viewModel: FlashScoreViewModel) :
    RecyclerView.Adapter<PlayersAdapter.PlayersViewHolder>() {

    inner class PlayersViewHolder(val binding: PlayerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem.playerKey == newItem.playerKey
        }

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var players: List<Player>
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
