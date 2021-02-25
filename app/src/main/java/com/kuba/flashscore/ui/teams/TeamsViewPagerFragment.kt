package com.kuba.flashscore.ui.teams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.ViewPagerAdapter
import com.kuba.flashscore.databinding.FragmentTeamsViewPagerBinding
import com.kuba.flashscore.local.models.entities.CountryAndLeagues
import com.kuba.flashscore.other.Constants.MATCHES_TAB
import com.kuba.flashscore.other.Constants.RESULT_TAB
import com.kuba.flashscore.other.Constants.TABLE_TAB
import com.kuba.flashscore.other.Constants.TEAMS_TAB
import com.kuba.flashscore.ui.teams.standings.StandingsViewPagerFragment

class TeamsViewPagerFragment : Fragment(R.layout.fragment_teams_view_pager) {

    private var _binding: FragmentTeamsViewPagerBinding? = null
    private val binding get() = _binding!!

    private val args: TeamsViewPagerFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTeamsViewPagerBinding.inflate(inflater, container, false)
        val view = binding.root

        val countryAndLeague = args.countryAndLeague

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = countryAndLeague.leagues[0].leagueName
            subtitle = ""
        }

        setInformationAboutCountryAndLeague(countryAndLeague)

        setTeamsViewPageAdapterAndTabLayout(countryAndLeague)

        return view
    }


    private fun setInformationAboutCountryAndLeague(countryAndLeague: CountryAndLeagues) {
        binding.apply {
            textViewCountryName.text = countryAndLeague.country.countryName
            Glide.with(requireContext()).load(countryAndLeague.country.countryLogo).into(imageViewCountryFlag)
            textViewLeagueName.text = countryAndLeague.leagues[0].leagueName
            Glide.with(requireContext()).load(countryAndLeague.leagues[0].leagueLogo).into(imageViewLeagueLogo)
        }
    }
    private fun setTeamsViewPageAdapterAndTabLayout(countryAndLeague: CountryAndLeagues) {
        val teamFragmentList = arrayListOf<Fragment>(
            TeamsFragment(countryAndLeague),
            StandingsViewPagerFragment(countryAndLeague)
        )

        val teamViewPagerAdapter = ViewPagerAdapter(
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
                    tab.text = TEAMS_TAB
                }
                1 -> {
                    tab.text = TABLE_TAB
                }
                2 -> {
                    tab.text = RESULT_TAB
                }
                3 -> {
                    tab.text = MATCHES_TAB
                }
            }
        }.attach()
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