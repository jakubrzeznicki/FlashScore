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
import com.kuba.flashscore.databinding.FragmentClubViewPagerBinding
import com.kuba.flashscore.local.models.entities.CountryAndLeagues
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
        val teamName = args.teamName
        val teamBadge = args.teamBadge
        val countryAndLeague = args.countryAndLeagueItem

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = teamName
            subtitle = ""
        }

        setInformationAboutCountryAndLeague(countryAndLeague, teamName, teamBadge)

        setClubViewPageAdapterAndTabLayout(teamId, teamName, teamBadge)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setClubViewPageAdapterAndTabLayout(
        teamId: String,
        teamName: String,
        teamBadge: String
    ) {
        val clubFragmentList = arrayListOf<Fragment>(
            PlayersFragment(teamId, teamName, teamBadge)
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
        countryAndLeagues: CountryAndLeagues,
        teamName: String,
        teamBadge: String
    ) {
        binding.apply {
            textViewCountryName.text = countryAndLeagues.country.countryName
            Glide.with(requireContext()).load(countryAndLeagues.country.countryLogo).into(imageViewCountryFlag)
            textViewClubName.text = teamName
            Glide.with(requireContext()).load(teamBadge).into(imageViewClubLogo)
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