package com.kuba.flashscore.ui.teams.standings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.ViewPagerAdapter
import com.kuba.flashscore.data.domain.models.customs.CountryWithLeagueAndTeams
import com.kuba.flashscore.databinding.FragmentStandingsViewPagerBinding
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.other.Constants.AWAY_TAB
import com.kuba.flashscore.other.Constants.GENERALLY_TAB
import com.kuba.flashscore.other.Constants.HOME_TAB
import com.kuba.flashscore.ui.teams.TeamsViewModel
import com.kuba.flashscore.ui.teams.standings.away.StandingsAwayFragment
import com.kuba.flashscore.ui.teams.standings.home.StandingsHomeFragment
import com.kuba.flashscore.ui.teams.standings.overall.StandingsOverallFragment

class StandingsViewPagerFragment :
    Fragment(R.layout.fragment_standings_view_pager) {

    private var _binding: FragmentStandingsViewPagerBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: TeamsViewModel
    private lateinit var countryWithLeagueAndTeams: CountryWithLeagueAndTeams

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStandingsViewPagerBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(requireActivity()).get(TeamsViewModel::class.java)

        subscribeToObservers()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeToObservers() {
        viewModel.teams.observe(viewLifecycleOwner, Observer { countryWithLeagueAndTeams ->
            if (countryWithLeagueAndTeams != null) {
                setStandingsViewPageAdapterAndTabLayout(countryWithLeagueAndTeams)
            }
        })
    }


    private fun setStandingsViewPageAdapterAndTabLayout(countryWithLeagueAndTeams: CountryWithLeagueAndTeams) {

        val standingsFragmentList = arrayListOf<Fragment>(
            StandingsOverallFragment(countryWithLeagueAndTeams),
            StandingsHomeFragment(countryWithLeagueAndTeams),
            StandingsAwayFragment(countryWithLeagueAndTeams)
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