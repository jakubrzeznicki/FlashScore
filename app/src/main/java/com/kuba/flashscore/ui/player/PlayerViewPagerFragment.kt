package com.kuba.flashscore.ui.player

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.kuba.flashscore.R
import com.kuba.flashscore.databinding.FragmentPlayerViewPagerBinding
import com.kuba.flashscore.network.models.LeagueDto
import com.kuba.flashscore.network.models.PlayerDto
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*

class PlayerViewPagerFragment : Fragment(R.layout.fragment_player_view_pager) {

    private var _binding: FragmentPlayerViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerViewPagerBinding.inflate(inflater, container, false)
        val view = binding.root

        val teamName = PlayerViewPagerFragmentArgs.fromBundle(requireArguments()).teamName
        val teamLogo = PlayerViewPagerFragmentArgs.fromBundle(requireArguments()).teamLogo
        val player = PlayerViewPagerFragmentArgs.fromBundle(requireArguments()).playerItem

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = player.playerName
        }

        val playerFragmentList = arrayListOf<Fragment>(
            PlayerCurrentSeasonDetailFragment(player)
        )

        setInformationAboutPlayer(teamName, teamLogo, player)


        val playerViewPagerAdapter = PlayerViewPagerAdapter(
            playerFragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPagerPlayerDetails.adapter = playerViewPagerAdapter

        TabLayoutMediator(
            binding.tabLayoutPlayerDetails, binding.viewPagerPlayerDetails
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Obecny sezon"
                }
            }
        }.attach()

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun setInformationAboutPlayer(
        teamName: String,
        teamLogo: String,
        player: PlayerDto
    ) {
        binding.apply {
            textViewCountryName.text = player.playerCountry
            Glide.with(requireContext()).load(teamLogo).into(imageViewCountryFlag)
            Glide.with(requireContext())
                .load(ContextCompat.getDrawable(requireContext(), R.drawable.ic_person))
                .into(imageViewPlayerPhoto)
            textViewPlayerName.text = player.playerName
            textViewPlayerClubAndPosition.text =
                "${teamName.toUpperCase(Locale.ROOT)}  (${player.playerType.take(player.playerType.length - 1)})"
            textViewPlayerAge.text = "Age: ${player.playerAge}"
            textViewPlayerNumber.text = "Number: ${player.playerNumber}"
            Glide.with(requireContext()).load(teamLogo).into(imageViewClubLogo)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}