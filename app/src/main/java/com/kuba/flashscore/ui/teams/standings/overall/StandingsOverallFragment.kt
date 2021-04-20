package com.kuba.flashscore.ui.teams.standings.overall

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.StandingsAdapter
import com.kuba.flashscore.data.domain.models.customs.CountryWithLeagueAndTeams
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.TeamEntity
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity
import com.kuba.flashscore.databinding.FragmentStandingsOverallBinding
import com.kuba.flashscore.other.Constants.DEFAULT_ERROR_MESSAGE
import com.kuba.flashscore.other.Constants.OVERALL
import com.kuba.flashscore.other.Constants.SUCCESS_MESSAGE
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.ui.teams.TeamsViewModel
import com.kuba.flashscore.ui.teams.club.ClubViewPagerFragmentArgs
import com.kuba.flashscore.ui.teams.standings.StandingsViewModel
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class StandingsOverallFragment :
    Fragment(R.layout.fragment_standings_overall) {

    private var _binding: FragmentStandingsOverallBinding? = null
    private val binding get() = _binding!!

    lateinit var standingsViewModel: StandingsViewModel
    lateinit var standingsAdapter: StandingsAdapter

    private lateinit var countryWithLeagueAndTeams: CountryWithLeagueAndTeams

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        standingsViewModel =
            ViewModelProvider(requireActivity()).get(StandingsViewModel::class.java)


        standingsViewModel.getOverallStandingsFromSpecificLeague(countryWithLeagueAndTeams.leagueWithTeams[0].league.leagueId)
        subscribeToObservers(countryWithLeagueAndTeams)
        setupRecyclerView(countryWithLeagueAndTeams)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun setCountryWithLeagueAndTeams(value: CountryWithLeagueAndTeams) {
        countryWithLeagueAndTeams = value
    }

    private fun subscribeToObservers(countryWithLeagueAndTeams: CountryWithLeagueAndTeams) {
        standingsViewModel.standings.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                refreshStandings(countryWithLeagueAndTeams.leagueWithTeams[0].league.leagueId)
            } else {
                standingsAdapter.standings =
                    it.sortedBy { standing -> standing.leaguePosition.toInt() }
            }
        })
        standingsViewModel.standingsStatus.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        Snackbar.make(
                            requireView(),
                            SUCCESS_MESSAGE,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                            requireView(),
                            result.message ?: DEFAULT_ERROR_MESSAGE,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {
                    }
                }
            }
        })
    }

    private fun refreshStandings(leagueId: String) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            standingsViewModel.refreshStandingsFromSpecificLeague(leagueId)
            standingsViewModel.getOverallStandingsFromSpecificLeague(leagueId)
        }
    }

    private fun setupRecyclerView(countryWithLeagueAndTeams: CountryWithLeagueAndTeams) {
        binding.recyclerViewOverallStandings.apply {
            standingsAdapter =
                StandingsAdapter(requireContext(), countryWithLeagueAndTeams, OVERALL)
            adapter = standingsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                HorizontalDividerItemDecoration.Builder(requireContext())
                    .color(ContextCompat.getColor(requireContext(), R.color.secondaryTextColor))
                    .sizeResId(R.dimen.divider)
                    .build()
            )
        }
    }
}