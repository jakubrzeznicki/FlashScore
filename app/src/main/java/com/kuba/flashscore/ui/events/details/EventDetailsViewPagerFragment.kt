package com.kuba.flashscore.ui.events.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.ViewPagerAdapter
import com.kuba.flashscore.data.domain.models.customs.TeamWithPlayersAndCoach
import com.kuba.flashscore.data.domain.models.event.customs.CountryWithLeagueWithEventsAndTeams
import com.kuba.flashscore.data.domain.models.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions
import com.kuba.flashscore.data.domain.models.event.customs.EventWithEventInformation
import com.kuba.flashscore.databinding.FragmentEventDetailsViewPagerBinding
import com.kuba.flashscore.data.local.models.entities.customs.TeamWithPlayersAndCoachEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.CountryWithLeagueWithEventsAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity
import com.kuba.flashscore.other.Constants.EVENT_DERAILS_TAB
import com.kuba.flashscore.other.Constants.EVENT_HEAD_2_HEAD_TAB
import com.kuba.flashscore.other.Constants.EVENT_STATISTICS_TAB
import com.kuba.flashscore.other.Constants.EVENT_TABLE_TAB
import com.kuba.flashscore.other.Constants.EVENT_TEAM_TAB
import com.kuba.flashscore.ui.events.EventsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class EventDetailsViewPagerFragment : Fragment(R.layout.fragment_event_details_view_pager) {

    private var _binding: FragmentEventDetailsViewPagerBinding? = null
    private val binding get() = _binding!!

    private val args: EventDetailsViewPagerFragmentArgs by navArgs()

    private lateinit var countryWithLeagueWithEventsAndTeams: CountryWithLeagueWithEventsAndTeams
    private lateinit var eventId: String

    lateinit var viewModel: EventsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventDetailsViewPagerBinding.inflate(inflater, container, false)
        val view = binding.root

        countryWithLeagueWithEventsAndTeams = args.countryWithLeagueWithTeamsAndEvents
        eventId = args.eventId

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = ""
            subtitle = ""
        }

        setInformationAboutTeamsAndScore(countryWithLeagueWithEventsAndTeams, eventId)
        setInformationAboutCountryAndLeague(countryWithLeagueWithEventsAndTeams, eventId)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(EventsViewModel::class.java)
        getEventDetails(eventId)
        //viewModel.getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(eventId)

        getHomeTeamWithPlayersAndCoach(
            countryWithLeagueWithEventsAndTeams.leagueWithEvents[0].eventsWithEventInformation.firstOrNull {
                it.event.matchId == eventId
            }?.eventInformation?.get(
                0
            )?.teamId!!
        )
        getAwayTeamWithPlayersAndCoach(
            countryWithLeagueWithEventsAndTeams.leagueWithEvents[0].eventsWithEventInformation.firstOrNull {
                it.event.matchId == eventId
            }?.eventInformation?.get(
                1
            )?.teamId!!
        )
        subscribeToObservers(countryWithLeagueWithEventsAndTeams.leagueWithEvents[0].eventsWithEventInformation.firstOrNull { it.event.matchId == eventId }!!)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setEventViewPageAdapterAndTabLayout(
        eventWithEventInformation: EventWithEventInformation,
        eventsWithDetails: EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions,
        homeTeam: TeamWithPlayersAndCoach,
        awayTeam: TeamWithPlayersAndCoach
    ) {

        val eventFragmentList = arrayListOf<Fragment>(
            EventDetailsFragment(eventWithEventInformation, eventsWithDetails, homeTeam, awayTeam),
            EventStatisticsFragment(eventsWithDetails, homeTeam, awayTeam),
            EventTeamsFragment(eventsWithDetails, homeTeam, awayTeam),
            EventHead2HeadFragment(eventsWithDetails, homeTeam, awayTeam),
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
    private fun setInformationAboutCountryAndLeague(
        event: CountryWithLeagueWithEventsAndTeams,
        eventId: String
    ) {
        binding.apply {
            val eventItem =
                event.leagueWithEvents[0].eventsWithEventInformation.firstOrNull { it.event.matchId == eventId }

            textViewCountryName.text = event.country.countryName
            Glide.with(requireContext()).load(event.country.countryLogo)
                .into(imageViewCountryFlag)
            textViewLeagueName.text =
                "${event.leagueWithEvents[0].league.leagueName} - ${eventItem?.event?.matchRound}"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setInformationAboutTeamsAndScore(
        event: CountryWithLeagueWithEventsAndTeams,
        eventId: String
    ) {
        binding.apply {
            val eventItem =
                event.leagueWithEvents[0].eventsWithEventInformation.firstOrNull { it.event.matchId == eventId }
            val teamHomeItem =
                event.leagueWithTeams[0].teams.firstOrNull {
                    it.teamKey == eventItem?.eventInformation?.get(
                        0
                    )?.teamId
                }
            val teamAwayItem =
                event.leagueWithTeams[0].teams.firstOrNull {
                    it.teamKey == eventItem?.eventInformation?.get(
                        1
                    )?.teamId
                }


            Glide.with(requireContext()).load(teamHomeItem?.teamBadge).into(imageViewFirstClubLogo)
            textViewFirstClubName.text = teamHomeItem?.teamName

            Glide.with(requireContext()).load(teamAwayItem?.teamBadge).into(imageViewSecondClubLogo)
            textViewSecondClubName.text = teamAwayItem?.teamName

            textViewDateAndTime.text =
                "${eventItem?.event?.matchDate}  ${eventItem?.event?.matchTime}"

            if (eventItem?.eventInformation?.get(0)?.teamScore?.isNotEmpty()!!) {
                textViewFirstScore.text = eventItem.eventInformation[0].teamScore
                textViewSecondScore.text = eventItem.eventInformation[1].teamScore
            }
        }
    }

    private fun getEventDetails(eventId: String) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            Timber.d("EVENT DETAILL get evnt details")
            viewModel.getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(eventId)
        }
    }

    private fun getHomeTeamWithPlayersAndCoach(teamId: String) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            Timber.d("EVENT DETAILL get home team")
            viewModel.getPlayersAndCoachFromHomeTeam(teamId)
        }
    }

    private fun getAwayTeamWithPlayersAndCoach(teamId: String) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            Timber.d("EVENT DETAILL get away team")
            viewModel.getPlayersAndCoachFromAwayTeam(teamId)
        }
    }

    private fun subscribeToObservers(eventWithEventInformation: EventWithEventInformation) {
        viewModel.eventsWithDetailsWithHomeAndAwayTeams.observe(viewLifecycleOwner, Observer {
            Timber.d("EVENT DETAILLL ${it.first.team.teamName}, ${it.second.players.size}")
            setEventViewPageAdapterAndTabLayout(
                eventWithEventInformation,
                it.third,
                it.first,
                it.second
            )
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