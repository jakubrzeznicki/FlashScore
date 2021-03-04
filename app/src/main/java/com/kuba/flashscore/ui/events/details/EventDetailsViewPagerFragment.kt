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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.ViewPagerAdapter
import com.kuba.flashscore.databinding.FragmentEventDetailsViewPagerBinding
import com.kuba.flashscore.local.models.entities.TeamWithPlayersAndCoach
import com.kuba.flashscore.local.models.entities.event.CountryWithLeagueWithEventsAndTeams
import com.kuba.flashscore.local.models.entities.event.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions
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
        getHomeTeamWithPlayersAndCoach(event.leagueWithEvents[0].events.filter { it?.matchId == eventId }[0]?.matchHometeamId!!)
        getAwayTeamWithPlayersAndCoach(event.leagueWithEvents[0].events.filter { it?.matchId == eventId }[0]?.matchAwayteamId!!)
        subscribeToObservers()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setEventViewPageAdapterAndTabLayout(
        eventsWithDetails: EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions,
        homeTeam: TeamWithPlayersAndCoach,
        awayTeam: TeamWithPlayersAndCoach
    ) {

        val eventFragmentList = arrayListOf<Fragment>(
            EventDetailsFragment(eventsWithDetails, homeTeam, awayTeam),
            EventStatisticsFragment(eventsWithDetails, homeTeam, awayTeam),
            EventTeamsFragment(eventsWithDetails, homeTeam, awayTeam),
            EventHead2HeadFragment(eventsWithDetails, homeTeam, awayTeam),
            EventDetailsFragment(eventsWithDetails, homeTeam, awayTeam)
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

    private fun getHomeTeamWithPlayersAndCoach(teamId: String) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getPlayersAndCoachFromHomeTeam(teamId)
        }
    }

    private fun getAwayTeamWithPlayersAndCoach(teamId: String) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getPlayersAndCoachFromAwayTeam(teamId)
        }
    }

    private fun subscribeToObservers() {
//        viewModel.eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions.observe(
//            viewLifecycleOwner, androidx.lifecycle.Observer {
//                Timber.d("DEJZIII ${it.eventEntity.matchId}")
//                Timber.d("DEJZIII ${it.cards.size}")
//                Timber.d("DEJZIII ${it.cards.forEach { Timber.d("DEJZIII cardId, card: ${it.card}, matchId: ${it.matchId}") }}")
//                eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions = it
//            })
//
//        viewModel.homeTeamWithPlayersAndCoach.observe(viewLifecycleOwner, Observer {
//            homeTeamWithPlayersAndCoach = it
//        })
//
//        viewModel.awayTeamWithPlayersAndCoach.observe(viewLifecycleOwner, Observer {
//            awayTeamWithPlayersAndCoach = it
//        })

//        viewModel.aaa.observe(viewLifecycleOwner, Observer {
//            Timber.d("DEJZIII mediator home team name: ${it[0]?.team?.teamName}, away team name: ${it[1]?.team?.teamName}")
//        })

        viewModel.eventsWithDetailsWithHomeAndAwayTeams.observe(viewLifecycleOwner, Observer {
            Timber.d("DEJZIII mediator home team name: ${it.first.team.teamName}, away team name: ${it.second.team.teamName}, event: ${it.third.eventEntity.matchId}")
            setEventViewPageAdapterAndTabLayout(it.third, it.first, it.second)
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