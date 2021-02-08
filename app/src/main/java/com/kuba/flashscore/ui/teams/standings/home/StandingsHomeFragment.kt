package com.kuba.flashscore.ui.teams.standings.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.StandingsAdapter
import com.kuba.flashscore.databinding.FragmentStandingsAwayBinding
import com.kuba.flashscore.databinding.FragmentStandingsHomeBinding
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.ui.FlashScoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class StandingsHomeFragment(private val leagueId: String) :
    Fragment(R.layout.fragment_standings_home) {

    private var _binding: FragmentStandingsHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FlashScoreViewModel by viewModels()
    private lateinit var standingsAdapter: StandingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStandingsHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getStandings(leagueId)
        subscribeToObservers()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeToObservers() {
        viewModel.standings.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val standings = result.data
                        if (standings != null) {
                            standingsAdapter.standings = standings
                        }
                        standings?.forEach {
                            Timber.d("Standings: ${standings}")
                        }
                    }
                    Status.ERROR -> {
                    }
                    Status.LOADING -> {
                    }
                }
            }
        })

    }

    private fun getStandings(leagueId: String) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getStandingsFromSpecificLeague(leagueId)
            setupRecyclerView()
        }
    }


    private fun setupRecyclerView() {
        binding.recyclerViewHomeStandings.apply {
            standingsAdapter = StandingsAdapter(requireContext(), viewModel, "home")
            adapter = standingsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}