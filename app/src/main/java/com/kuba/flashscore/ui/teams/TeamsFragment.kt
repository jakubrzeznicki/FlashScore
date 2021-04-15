package com.kuba.flashscore.ui.teams

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.TeamsAdapter
import com.kuba.flashscore.data.domain.models.customs.CountryWithLeagueAndTeams
import com.kuba.flashscore.databinding.FragmentTeamsBinding
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration

class TeamsFragment() :
    Fragment(R.layout.fragment_teams) {

    private var _binding: FragmentTeamsBinding? = null
    private val binding get() = _binding!!

    lateinit var teamsAdapter: TeamsAdapter

    lateinit var viewModel: TeamsViewModel

    private lateinit var countryWithLeagueAndTeams: CountryWithLeagueAndTeams



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTeamsBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(requireActivity()).get(TeamsViewModel::class.java)

        setupRecyclerView()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.recyclerViewTeams.apply {
            teamsAdapter = TeamsAdapter(countryWithLeagueAndTeams)
            teamsAdapter.teams = countryWithLeagueAndTeams.leagueWithTeams[0].teams
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

    fun setCountryWithLeagueAndTeams(countryWithLeagueAndTeamsValue: CountryWithLeagueAndTeams) {
        countryWithLeagueAndTeams = countryWithLeagueAndTeamsValue
    }
}