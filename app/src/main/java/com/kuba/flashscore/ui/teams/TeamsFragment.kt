package com.kuba.flashscore.ui.teams

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
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.TeamsAdapter
import com.kuba.flashscore.databinding.FragmentTeamsBinding
import com.kuba.flashscore.local.models.entities.CountryAndLeagues
import com.kuba.flashscore.local.models.entities.LeagueEntity
import com.kuba.flashscore.other.Status
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeamsFragment(private val countryAndLeagues: CountryAndLeagues) : Fragment(R.layout.fragment_teams) {

    private var _binding: FragmentTeamsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TeamsViewModel by viewModels()
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

        getTeams(countryAndLeagues.leagues[0].leagueId)
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
            teamsAdapter = TeamsAdapter(countryAndLeagues.leagues[0])
            adapter = teamsAdapter
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