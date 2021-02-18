package com.kuba.flashscore.ui.league

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.LeagueAdapter
import com.kuba.flashscore.databinding.FragmentLeagueBinding
import com.kuba.flashscore.local.models.entities.CountryEntity
import com.kuba.flashscore.network.models.CountryDto
import com.kuba.flashscore.other.Status
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LeagueFragment : Fragment(R.layout.fragment_league) {

    private var _binding: FragmentLeagueBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LeagueViewModel by viewModels()
    private lateinit var leagueAdapter: LeagueAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLeagueBinding.inflate(inflater, container, false)
        val view = binding.root

        val country = LeagueFragmentArgs.fromBundle(requireArguments()).countryItem

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = country.countryName
        }
        setInformationAboutCountry(country)

        getLeagues(country.countryId)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            leagueAdapter = LeagueAdapter()
            adapter = leagueAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                HorizontalDividerItemDecoration.Builder(requireContext())
                    .color(ContextCompat.getColor(requireContext(), R.color.secondaryTextColor))
                    .sizeResId(R.dimen.divider)
                    .build()
            )
        }
    }

    private fun setInformationAboutCountry(country: CountryEntity) {
        binding.apply {
            textViewCountryName.text = country.countryName
            Glide.with(requireContext()).load(country.countryLogo).into(imageViewCountryFlag)
        }
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