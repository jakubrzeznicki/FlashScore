package com.kuba.flashscore.ui.teams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.kuba.flashscore.R
import com.kuba.flashscore.databinding.FragmentTeamsViewPagerBinding
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

        val leagueId = TeamsViewPagerFragmentArgs.fromBundle(requireArguments()).leagueId
        val teamFragmentList = arrayListOf<Fragment>(
            TeamsFragment(leagueId),
            StandingsViewPagerFragment(leagueId)
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

}