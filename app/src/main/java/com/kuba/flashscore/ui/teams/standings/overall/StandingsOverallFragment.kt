package com.kuba.flashscore.ui.teams.standings.overall

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.StandingsAdapter
import com.kuba.flashscore.data.domain.models.customs.CountryWithLeagueAndTeams
import com.kuba.flashscore.databinding.FragmentStandingsOverallBinding
import com.kuba.flashscore.other.Constants.OVERALL
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.ui.teams.standings.StandingsViewModel
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class StandingsOverallFragment :
    Fragment(R.layout.fragment_standings_overall) {

    private var _binding: FragmentStandingsOverallBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StandingsViewModel by viewModels()
    private lateinit var standingsAdapter: StandingsAdapter

    private lateinit var countryWithLeagueAndTeams: CountryWithLeagueAndTeams

    fun setCountryWithLeagueAndTeams(value: CountryWithLeagueAndTeams) {
        countryWithLeagueAndTeams = value
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStandingsOverallBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getOverallStandingsFromSpecificLeague(countryWithLeagueAndTeams.leagueWithTeams[0].league.leagueId)
        setupRecyclerView()
        subscribeToObservers()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeToObservers() {
        viewModel.standings.observe(viewLifecycleOwner, Observer {
            Timber.d("STANDINGS OVERALL gert form db")
            if (it.isNullOrEmpty()) {
                Timber.d("STANDINGS OVERALL gert form db are null")
                refreshStandings(countryWithLeagueAndTeams.leagueWithTeams[0].league.leagueId)
            } else {
                Timber.d("STANDINGS OVERALL gert form db are not null $it")
                standingsAdapter.standings =
                    it.sortedBy { standing -> standing.leaguePosition.toInt() }
            }
        })
        viewModel.standingsStatus.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        Snackbar.make(
                            requireView(),
                            "Successfully fetched data from network",
                            Snackbar.LENGTH_LONG
                        ).show()
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

    private fun refreshStandings(leagueId: String) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.refreshStandingsFromSpecificLeague(leagueId)
            delay(1000)
            viewModel.getOverallStandingsFromSpecificLeague(leagueId)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewOverallStandings.apply {
            standingsAdapter = StandingsAdapter(requireContext(), countryWithLeagueAndTeams, OVERALL)
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