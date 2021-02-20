package com.kuba.flashscore.ui.teams.standings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.ViewPagerAdapter
import com.kuba.flashscore.databinding.FragmentStandingsViewPagerBinding
import com.kuba.flashscore.local.models.entities.CountryAndLeagues
import com.kuba.flashscore.local.models.entities.LeagueEntity
import com.kuba.flashscore.network.models.LeagueDto
import com.kuba.flashscore.other.Constants.AWAY_TAB
import com.kuba.flashscore.other.Constants.GENERALLY_TAB
import com.kuba.flashscore.other.Constants.HOME_TAB
import com.kuba.flashscore.ui.teams.standings.away.StandingsAwayFragment
import com.kuba.flashscore.ui.teams.standings.home.StandingsHomeFragment
import com.kuba.flashscore.ui.teams.standings.overall.StandingsOverallFragment

class StandingsViewPagerFragment(private val countryAndLeagues: CountryAndLeagues) :
    Fragment(R.layout.fragment_standings_view_pager) {

    private var _binding: FragmentStandingsViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStandingsViewPagerBinding.inflate(inflater, container, false)
        val view = binding.root
        setStandingsViewPageAdapterAndTabLayout()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setStandingsViewPageAdapterAndTabLayout() {

        val standingsFragmentList = arrayListOf<Fragment>(
            StandingsOverallFragment(countryAndLeagues.leagues[0]),
            StandingsHomeFragment(countryAndLeagues.leagues[0]),
            StandingsAwayFragment(countryAndLeagues.leagues[0])
        )

        val standingsViewPagerAdapter = ViewPagerAdapter(
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
                    tab.text = GENERALLY_TAB
                }
                1 -> {
                    tab.text = HOME_TAB
                }
                2 -> {
                    tab.text = AWAY_TAB
                }
            }
        }.attach()
    }

}