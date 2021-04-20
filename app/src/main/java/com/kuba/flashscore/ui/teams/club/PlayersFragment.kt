package com.kuba.flashscore.ui.teams.club

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.PlayersAdapter
import com.kuba.flashscore.data.domain.models.Team
import com.kuba.flashscore.data.domain.models.customs.TeamWithPlayersAndCoach
import com.kuba.flashscore.databinding.FragmentPlayersBinding
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.ui.teams.TeamsViewModel
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class PlayersFragment(private val team: Team) : Fragment(R.layout.fragment_players) {
    private var _binding: FragmentPlayersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TeamsViewModel by viewModels()
    private lateinit var playerAdapter: PlayersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayersBinding.inflate(inflater, container, false)

        playerAdapter = PlayersAdapter(team)
        getTeamWithPlayersAndCoaach(team.teamKey)
        subscribeToObservers()
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel.getTeamWithPlayersAndCoach(team.teamKey)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeToObservers() {
        viewModel.team.observe(viewLifecycleOwner, Observer {
            playerAdapter.players = it.players
        })

    }

    private fun getTeamWithPlayersAndCoaach(teamId: String) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getTeamWithPlayersAndCoach(teamId)
        }

    }

    private fun setupRecyclerView() {
        binding.recyclerViewTeams.apply {
            adapter = playerAdapter
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