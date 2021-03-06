package com.kuba.flashscore.ui.events.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kuba.flashscore.R
import com.kuba.flashscore.data.domain.models.customs.TeamWithPlayersAndCoach
import com.kuba.flashscore.data.domain.models.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions
import com.kuba.flashscore.databinding.FragmentEventTeamsBinding
import com.kuba.flashscore.data.local.models.entities.customs.TeamWithPlayersAndCoachEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity


class EventTeamsFragment(
    private val eventDetails: EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions,
    private val homeTeam: TeamWithPlayersAndCoach,
    private val awayTeam: TeamWithPlayersAndCoach
) : Fragment(R.layout.fragment_event_teams) {

    private var _binding: FragmentEventTeamsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventTeamsBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }


}