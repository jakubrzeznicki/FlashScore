package com.kuba.flashscore.ui.teams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.kuba.flashscore.R
import com.kuba.flashscore.databinding.FragmentTeamsViewPagerBinding
import com.kuba.flashscore.network.models.LeagueDto
import com.kuba.flashscore.ui.teams.standings.StandingsViewPagerFragment

class TeamsViewPagerFragment : Fragment(R.layout.fragment_teams_view_pager) {

    private var _binding: FragmentTeamsViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTeamsViewPagerBinding.inflate(inflater, container, false)
        val view = binding.root

        val league = TeamsViewPagerFragmentArgs.fromBundle(requireArguments()).leagueItem

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = league.leagueName
        }

        setInformationAboutCountryAndLeague(league)

        val teamFragmentList = arrayListOf<Fragment>(
            TeamsFragment(league),
            StandingsViewPagerFragment(league)
        )

        val teamViewPagerAdapter = TeamsViewPagerAdapter(
            teamFragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPagerTeams.adapter = teamViewPagerAdapter

        TabLayoutMediator(
            binding.tabLayoutTeams2, binding.viewPagerTeams
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Drużyny"
                }
                1 -> {
                    tab.text = "Tabela"
                }
                2 -> {
                    tab.text = "Wyniki"
                }
                3 -> {
                    tab.text = "Spotkania"
                }
            }
        }.attach()

        return view
    }


    private fun setInformationAboutCountryAndLeague(league: LeagueDto) {
        binding.apply {
            textViewCountryName.text = league.countryName
            Glide.with(requireContext()).load(league.countryLogo).into(imageViewCountryFlag)
            textViewLeagueName.text = league.leagueName
            Glide.with(requireContext()).load(league.leagueLogo).into(imageViewLeagueLogo)

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