package com.kuba.flashscore.ui.league

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
import com.kuba.flashscore.adapters.CountryAdapter
import com.kuba.flashscore.adapters.LeagueAdapter
import com.kuba.flashscore.databinding.FragmentCountryBinding
import com.kuba.flashscore.databinding.FragmentLeagueBinding
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.ui.FlashScoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class LeagueFragment : Fragment(R.layout.fragment_league) {

    private var _binding: FragmentLeagueBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FlashScoreViewModel by viewModels()
    private lateinit var leagueAdapter: LeagueAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLeagueBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getLeagues(LeagueFragmentArgs.fromBundle(requireArguments()).countryId)
        subscribeToObservers()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun subscribeToObservers() {
        viewModel.leagues.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val leagues = result.data
                        if (leagues != null) {
                            leagueAdapter.league = leagues
                        }
                        leagues?.forEach {
                            Timber.d("Leagues: ${leagues}")
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

    private fun getLeagues(countryId: String) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getLeaguesFromSpecificCountry(countryId)
            setupRecyclerView()
        }

    }

    private fun setupRecyclerView() {
        binding.recyclerViewLeagues.apply {
            leagueAdapter = LeagueAdapter(requireContext(), viewModel)
            adapter = leagueAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}