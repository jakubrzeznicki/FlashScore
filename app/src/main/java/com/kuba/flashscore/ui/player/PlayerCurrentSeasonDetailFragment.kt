package com.kuba.flashscore.ui.player

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kuba.flashscore.R
import com.kuba.flashscore.databinding.FragmentPlayerCurrentSeasonDetailBinding
import com.kuba.flashscore.local.models.entities.PlayerEntity

import com.kuba.flashscore.network.models.PlayerDto

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerCurrentSeasonDetailFragment(private val player: PlayerEntity) :
    Fragment(R.layout.fragment_player_current_season_detail) {

    private var _binding: FragmentPlayerCurrentSeasonDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerCurrentSeasonDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        setDetailsAboutThisSeason()
        return view
    }

    private fun setDetailsAboutThisSeason() {
        binding.apply {
            textViewPlayedMatches.text = player.playerMatchPlayed
            textViewScoredGoals.text = player.playerGoals
            textViewYellowCards.text = player.playerYellowCards
            textViewRedCards.text = player.playerRedCards
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
