package com.kuba.flashscore.ui.teams.standings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.kuba.flashscore.R
import com.kuba.flashscore.data.local.entities.League
import com.kuba.flashscore.databinding.FragmentStandingsViewPagerBinding
import com.kuba.flashscore.ui.teams.standings.away.StandingsAwayFragment
import com.kuba.flashscore.ui.teams.standings.home.StandingsHomeFragment
import com.kuba.flashscore.ui.teams.standings.overall.StandingsOverallFragment

class StandingsViewPagerFragment(private val league: League) : Fragment(R.layout.fragment_standings_view_pager) {

    private var _binding: FragmentStandingsViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStandingsViewPagerBinding.inflate(inflater, container, false)
        val view = binding.root

        val standingsFragmentList = arrayListOf<Fragment>(
            StandingsOverallFragment(league),
            StandingsHomeFragment(league),
            StandingsAwayFragment(league)
        )

        val standingsViewPagerAdapter = StandingsViewPagerAdapter(
            standingsFragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPagerStandings.adapter = standingsViewPagerAdapter

        TabLayoutMediator(
            binding.tabLayoutStandings2, binding.viewPagerStandings
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Ogólna"
                }
                1 -> {
                    tab.text = "Dom"
                }
                2 -> {
                    tab.text = "Wyjazd"
                }
            }
        }.attach()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}