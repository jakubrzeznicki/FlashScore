package com.kuba.flashscore.ui.events.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.ViewPagerAdapter
import com.kuba.flashscore.databinding.FragmentEventDetailsViewPagerBinding
import com.kuba.flashscore.local.models.entities.event.CountryWithLeagueWithEventsAndTeams
import com.kuba.flashscore.local.models.entities.event.EventEntity
import com.kuba.flashscore.local.models.entities.event.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions
import com.kuba.flashscore.network.models.PlayerDto
import com.kuba.flashscore.network.models.events.EventDto
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.other.Constants.CURRENT_SEASON_TAB
import com.kuba.flashscore.other.Constants.EVENT_DERAILS_TAB
import com.kuba.flashscore.other.Constants.EVENT_HEAD_2_HEAD_TAB
import com.kuba.flashscore.other.Constants.EVENT_STATISTICS_TAB
import com.kuba.flashscore.other.Constants.EVENT_TABLE_TAB
import com.kuba.flashscore.other.Constants.EVENT_TEAM_TAB
import com.kuba.flashscore.other.Constants.PLAYER_AGE
import com.kuba.flashscore.other.Constants.PLAYER_NUMBER
import com.kuba.flashscore.ui.events.EventsListFragmentArgs
import com.kuba.flashscore.ui.events.EventsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class EventDetailsViewPagerFragment : Fragment(R.layout.fragment_event_details_view_pager) {

    private var _binding: FragmentEventDetailsViewPagerBinding? = null
    private val binding get() = _binding!!

    private val args: EventDetailsViewPagerFragmentArgs by navArgs()

    private val viewModel: EventsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventDetailsViewPagerBinding.inflate(inflater, container, false)
        val view = binding.root

        val event = args.eventItem
        val eventId = args.eventId

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = ""
            subtitle = ""
        }

        setInformationCountryLeagueAndScore(event, eventId)

        getEventDetails(eventId)
        subscribeToObservers()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setEventViewPageAdapterAndTabLayout(
        eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions:
        EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions
    ) {

        val eventFragmentList = arrayListOf<Fragment>(
            EventDetailsFragment(eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions),
            //EventStatisticsFragment(event),
            //EventTeamsFragment(event),
            //EventHead2HeadFragment(event),
            //EventDetailsFragment(event)
        )

        val eventViewPagerAdapter = ViewPagerAdapter(
            eventFragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPagerEventDetails.adapter = eventViewPagerAdapter

        TabLayoutMediator(
            binding.tabLayoutEventDetails, binding.viewPagerEventDetails
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = EVENT_DERAILS_TAB
                }
                1 -> {
                    tab.text = EVENT_STATISTICS_TAB
                }
                2 -> {
                    tab.text = EVENT_TABLE_TAB
                }
                3 -> {
                    tab.text = EVENT_HEAD_2_HEAD_TAB
                }
                4 -> {
                    tab.text = EVENT_TEAM_TAB
                }
            }
        }.attach()
    }

    @SuppressLint("SetTextI18n")
    private fun setInformationCountryLeagueAndScore(
        event: CountryWithLeagueWithEventsAndTeams,
        eventId: String
    ) {
        binding.apply {
            val eventItem = event.leagueWithEvents[0].events.filter { it?.matchId == eventId }[0]
            val teamHomeItem =
                event.leagueWithTeams[0].teams.filter { it.teamKey == eventItem?.matchHometeamId }[0]
            val teamAwayItem =
                event.leagueWithTeams[0].teams.filter { it.teamKey == eventItem?.matchAwayteamId }[0]
            textViewCountryName.text = event.countryEntity.countryName
            Glide.with(requireContext()).load(event.countryEntity.countryLogo)
                .into(imageViewCountryFlag)
            textViewLeagueName.text =
                "${event.leagueWithEvents[0].league.leagueName} - ${eventItem?.matchRound}"

            Glide.with(requireContext()).load(teamHomeItem.teamBadge).into(imageViewFirstClubLogo)
            textViewFirstClubName.text = teamHomeItem.teamName

            Glide.with(requireContext()).load(teamAwayItem.teamBadge).into(imageViewSecondClubLogo)
            textViewSecondClubName.text = teamAwayItem.teamName

            textViewDateAndTime.text = "${eventItem?.matchDate}  ${eventItem?.matchTime}"

            if (eventItem?.matchHometeamScore?.isNotEmpty()!!) {
                textViewFirstScore.text = eventItem.matchHometeamScore
                textViewSecondScore.text = eventItem.matchAwayteamScore
            }


        }
    }

    private fun getEventDetails(eventId: String) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(eventId)
        }
    }

    private fun subscribeToObservers() {
        viewModel.eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer {
                setEventViewPageAdapterAndTabLayout(it)
            })
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