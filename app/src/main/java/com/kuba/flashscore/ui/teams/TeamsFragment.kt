package com.kuba.flashscore.ui.teams

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
import com.kuba.flashscore.adapters.LeagueAdapter
import com.kuba.flashscore.adapters.TeamsAdapter
import com.kuba.flashscore.databinding.FragmentLeagueBinding
import com.kuba.flashscore.databinding.FragmentTeamsBinding
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.ui.FlashScoreViewModel
import com.kuba.flashscore.ui.league.LeagueFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class TeamsFragment(private val leagueId: String) : Fragment(R.layout.fragment_teams) {

    private var _binding: FragmentTeamsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FlashScoreViewModel by viewModels()
    private lateinit var teamsAdapter: TeamsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTeamsBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getTeams(leagueId)
        subscribeToObservers()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun subscribeToObservers() {
        viewModel.teams.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val teams = result.data
                        if (teams != null) {
                            teamsAdapter.teams = teams
                        }
                        teams?.forEach {
                            Timber.d("Leagues: ${teams}")
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

    private fun getTeams(leagueId: String) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getTeamsFormSpecificLeague(leagueId)
            setupRecyclerView()
        }

    }

    private fun setupRecyclerView() {
        binding.recyclerViewTeams.apply {
            teamsAdapter = TeamsAdapter(requireContext(), viewModel)
            adapter = teamsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}