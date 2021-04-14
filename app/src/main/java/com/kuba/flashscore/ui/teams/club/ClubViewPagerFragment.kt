package com.kuba.flashscore.ui.teams.club

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
import com.kuba.flashscore.data.domain.models.customs.CountryWithLeagueAndTeams
import com.kuba.flashscore.databinding.FragmentClubViewPagerBinding
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.other.Constants.TEAM_TAB

class ClubViewPagerFragment : Fragment(R.layout.fragment_club_view_pager) {

    private var _binding: FragmentClubViewPagerBinding? = null
    private val binding get() = _binding!!

    private val args: ClubViewPagerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClubViewPagerBinding.inflate(inflater, container, false)
        val view = binding.root

        val teamId = args.teamId
        val countryWithLeagueAndTeams = args.countryWithLeagueAndTeams

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarClubViewPager)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title =
                countryWithLeagueAndTeams.leagueWithTeams[0].teams.firstOrNull { it.teamKey == teamId }?.teamName
            subtitle = ""
        }

        setInformationAboutCountryAndLeague(teamId, countryWithLeagueAndTeams)

        setClubViewPageAdapterAndTabLayout(teamId, countryWithLeagueAndTeams)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setClubViewPageAdapterAndTabLayout(
        teamId: String,
        countryWithLeagueAndTeams: CountryWithLeagueAndTeams
    ) {
        val clubFragmentList = arrayListOf<Fragment>(
            PlayersFragment(countryWithLeagueAndTeams.leagueWithTeams[0].teams.firstOrNull { it.teamKey == teamId }!!)
        )
        val clubViewPagerAdapter = ViewPagerAdapter(
            clubFragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.viewPagerClub.adapter = clubViewPagerAdapter

        TabLayoutMediator(
            binding.tabLayoutClub, binding.viewPagerClub
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = TEAM_TAB
                }
            }
        }.attach()
    }

    private fun setInformationAboutCountryAndLeague(
        teamId: String,
        countryWithLeagueAndTeams: CountryWithLeagueAndTeams
    ) {
        binding.apply {
            textViewCountryName.text = countryWithLeagueAndTeams.country.countryName
            Glide.with(requireContext())
                .load(countryWithLeagueAndTeams.country.countryLogo)
                .into(imageViewCountryFlag)
            textViewClubName.text =
                countryWithLeagueAndTeams.leagueWithTeams[0].teams.firstOrNull { it.teamKey == teamId }?.teamName
            Glide.with(requireContext())
                .load(countryWithLeagueAndTeams.leagueWithTeams[0].teams.firstOrNull { it.teamKey == teamId }?.teamBadge)
                .into(imageViewClubLogo)
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