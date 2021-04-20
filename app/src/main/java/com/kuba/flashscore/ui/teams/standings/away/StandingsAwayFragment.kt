package com.kuba.flashscore.ui.teams.standings.away

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.StandingsAdapter
import com.kuba.flashscore.data.domain.models.Standing
import com.kuba.flashscore.data.domain.models.customs.CountryWithLeagueAndTeams
import com.kuba.flashscore.databinding.FragmentStandingsAwayBinding
import com.kuba.flashscore.other.Constants.AWAY
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.ui.teams.standings.StandingsViewModel
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class StandingsAwayFragment :
    Fragment(R.layout.fragment_standings_away) {

    private var _binding: FragmentStandingsAwayBinding? = null
    private val binding get() = _binding!!

    lateinit var standingsViewModel: StandingsViewModel
    lateinit var standingsAdapter: StandingsAdapter

    private lateinit var countryWithLeagueAndTeams: CountryWithLeagueAndTeams


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        standingsViewModel =
            ViewModelProvider(requireActivity()).get(StandingsViewModel::class.java)

        standingsViewModel.getAwayStandingsFromSpecificLeague(countryWithLeagueAndTeams.leagueWithTeams[0].league.leagueId)
        setupRecyclerView()
        subscribeToObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setCountryWithLeagueAndTeams(value: CountryWithLeagueAndTeams) {
        countryWithLeagueAndTeams = value
    }

    private fun subscribeToObservers() {
        standingsViewModel.standings.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                standingsAdapter.standings =
                    it.sortedBy { standing -> standing.leaguePosition.toInt() }
            }
        })
    }

    private fun setupRecyclerView() {
        binding.recyclerViewAwayStandings.apply {
            standingsAdapter = StandingsAdapter(requireContext(), countryWithLeagueAndTeams, AWAY)
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