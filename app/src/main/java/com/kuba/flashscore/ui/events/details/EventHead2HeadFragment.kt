package com.kuba.flashscore.ui.events.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kuba.flashscore.R
import com.kuba.flashscore.databinding.FragmentEventHead2HeadBinding
import com.kuba.flashscore.data.local.models.entities.TeamWithPlayersAndCoach
import com.kuba.flashscore.data.local.models.entities.event.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions


class EventHead2HeadFragment(
    private val eventDetails: EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions,
    private val homeTeam: TeamWithPlayersAndCoach,
    private val awayTeam: TeamWithPlayersAndCoach
) : Fragment(R.layout.fragment_event_head_2_head) {

    private var _binding: FragmentEventHead2HeadBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventHead2HeadBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }


}