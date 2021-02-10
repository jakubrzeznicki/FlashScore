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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.ViewPagerAdapter
import com.kuba.flashscore.databinding.FragmentEventDetailsViewPagerBinding
import com.kuba.flashscore.network.models.PlayerDto
import com.kuba.flashscore.network.models.events.EventDto
import com.kuba.flashscore.other.Constants.CURRENT_SEASON_TAB
import com.kuba.flashscore.other.Constants.EVENT_DERAILS_TAB
import com.kuba.flashscore.other.Constants.EVENT_HEAD_2_HEAD_TAB
import com.kuba.flashscore.other.Constants.EVENT_STATISTICS_TAB
import com.kuba.flashscore.other.Constants.EVENT_TABLE_TAB
import com.kuba.flashscore.other.Constants.EVENT_TEAM_TAB
import com.kuba.flashscore.other.Constants.PLAYER_AGE
import com.kuba.flashscore.other.Constants.PLAYER_NUMBER
import java.util.*

class EventDetailsViewPagerFragment : Fragment(R.layout.fragment_event_details_view_pager) {

    private var _binding: FragmentEventDetailsViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventDetailsViewPagerBinding.inflate(inflater, container, false)
        val view = binding.root

        val event = EventDetailsViewPagerFragmentArgs.fromBundle(requireArguments()).eventItem

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = ""
            subtitle = ""
        }

        setInformationCountryLeagueAndScore(event)

        setEventViewPageAdapterAndTabLayout(event)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setEventViewPageAdapterAndTabLayout(event: EventDto) {

        val eventFragmentList = arrayListOf<Fragment>(
            EventDetailsFragment(event),
            EventStatisticsFragment(event),
            EventTeamsFragment(event),
            EventHead2HeadFragment(event),
            EventDetailsFragment(event)
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
        event: EventDto
    ) {
        binding.apply {
            textViewCountryName.text = event.countryName
            Glide.with(requireContext()).load(event.countryLogo).into(imageViewCountryFlag)
            textViewLeagueName.text = "${event.leagueName} - ${event.matchRound}"

            Glide.with(requireContext()).load(event.teamHomeBadge).into(imageViewFirstClubLogo)
            textViewFirstClubName.text = event.matchHometeamName

            Glide.with(requireContext()).load(event.teamAwayBadge).into(imageViewSecondClubLogo)
            textViewSecondClubName.text = event.matchAwayteamName

            textViewDateAndTime.text = "${event.matchDate}  ${event.matchTime}"

            if (event.matchHometeamScore.isNotEmpty()) {
                textViewFirstScore.text = event.matchHometeamScore
                textViewSecondScore.text = event.matchAwayteamScore
            }


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