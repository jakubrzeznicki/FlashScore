package com.kuba.flashscore.ui.teams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.ViewPagerAdapter
import com.kuba.flashscore.databinding.FragmentTeamsViewPagerBinding
import com.kuba.flashscore.data.local.models.entities.CountryAndLeagues
import com.kuba.flashscore.data.local.models.entities.CountryWithLeagueAndTeams
import com.kuba.flashscore.other.Constants.MATCHES_TAB
import com.kuba.flashscore.other.Constants.RESULT_TAB
import com.kuba.flashscore.other.Constants.TABLE_TAB
import com.kuba.flashscore.other.Constants.TEAMS_TAB
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.ui.teams.standings.StandingsViewPagerFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeamsViewPagerFragment : Fragment(R.layout.fragment_teams_view_pager) {

    private var _binding: FragmentTeamsViewPagerBinding? = null
    private val binding get() = _binding!!

    private val args: TeamsViewPagerFragmentArgs by navArgs()
    private val viewModel: TeamsViewModel by viewModels()


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

        getCountryWithLeagueAndTeams(countryAndLeague.leagues[0].leagueId)
        subscribeToObservers()

        setInformationAboutCountryAndLeague(countryAndLeague)


        return view
    }

    private fun subscribeToObservers() {
        viewModel.teams.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val countryWithLeagueAndTeams = result.data
                        if (countryWithLeagueAndTeams != null) {
                            setTeamsViewPageAdapterAndTabLayout(countryWithLeagueAndTeams)
                        }
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                            requireView(),
                            result.message ?: "Default No Internet",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {
                    }
                }
            }
        })
    }

    private fun getCountryWithLeagueAndTeams(leagueId: String) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getCountryWithLeagueAndTeams(leagueId)
        }

    }


    private fun setInformationAboutCountryAndLeague(countryAndLeague: CountryAndLeagues) {
        binding.apply {
            textViewCountryName.text = countryAndLeague.country.countryName
            Glide.with(requireContext()).load(countryAndLeague.country.countryLogo).into(imageViewCountryFlag)
            textViewLeagueName.text = countryAndLeague.leagues[0].leagueName
            Glide.with(requireContext()).load(countryAndLeague.leagues[0].leagueLogo).into(imageViewLeagueLogo)
        }
    }
    private fun setTeamsViewPageAdapterAndTabLayout(countryWithLeagueAndTeams: CountryWithLeagueAndTeams) {
        val teamFragmentList = arrayListOf<Fragment>(
            TeamsFragment(countryWithLeagueAndTeams),
            StandingsViewPagerFragment(countryWithLeagueAndTeams)
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